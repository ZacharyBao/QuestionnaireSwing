import Entities.QuestionEntity;
import Utils.HttpClientHelper;
import Utils.ImagePanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionnaireForm extends JFrame {
    private JPanel rootPanel;
    private JPanel headPanel;
    private JPanel bottomPanel;
    private JPanel middlePanel;
    private JPanel middleTopPanel;
    private JPanel middleBottomPanel;
    private JLabel headLabel;
    private JLabel mainButton;
    private JTextPane questionText;
    private JList questionOptions;
    private JPanel middleBBottomPanel;
    private JPanel middleBTopPanel;

    private List<QuestionEntity> allQuestions;
    private HttpClientHelper httpClientHelper;
    private Long questionnaireId;
    private DefaultListModel listModel;
    private Map<String, String> selectedOptions;//store selected options
    private Integer questionIndexPointer;//record the current question number
    private String userId;
    private Map<String, Object> formParamsForAnswer;//map for saving answer
    private JLabel imgLabel;//display question img
    private String imgURL;
    private Integer windowsHeight;

    public QuestionnaireForm(String userid) {
        questionnaireId = 23L;//set questionnaire
        questionIndexPointer = 0;
        this.userId = userid;
        windowsHeight = 100;//Initial height should be 100, total height of headPanel and questionTextPanel
        listModel = new DefaultListModel();
        selectedOptions = new HashMap<String, String>();
        formParamsForAnswer = new HashMap<String, Object>();
        imgLabel = new JLabel();

        //Get questions
        httpClientHelper = new HttpClientHelper();
        allQuestions = httpClientHelper.getQuestionsByQuestionnaireId(questionnaireId);//load all questions from database

        initializeTextPanel();
        initializeImgPanel();
        initializeOptionJList();

        add(rootPanel);
        setSize(300, windowsHeight);
        //JList listener
        questionOptions.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int[] indices = questionOptions.getSelectedIndices();
                ListModel optionsListMode = questionOptions.getModel();
                selectedOptions.clear();
                for (int index : indices) {
                    String realIndex = "" + (index + 1);
                    selectedOptions.put(realIndex, (String) optionsListMode.getElementAt(index));
                }
                //System.out.println(selectedOptions);
            }
        });

        //Label listener
        mainButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (allQuestions.get(questionIndexPointer).getQuestionOptionalOptionNum() < selectedOptions.size()) {//selected option number has exceeded the regulated number
                    JOptionPane.showMessageDialog(null, "Please choose options within a particular number! ", "Warning", JOptionPane.ERROR_MESSAGE);
                } else if (selectedOptions.size() == 0) {//did not select options
                    JOptionPane.showMessageDialog(null, "Please choose your option(s) ", "Warning", JOptionPane.ERROR_MESSAGE);
                } else {//select options successfully
                    //save answer for the current question
                    formParamsForAnswer.clear();
                    formParamsForAnswer.put("question_id", allQuestions.get(questionIndexPointer).getQuestionId());
                    formParamsForAnswer.put("user_id", userId);
                    formParamsForAnswer.put("selected_options", selectedOptions);
                    formParamsForAnswer.put("questionnaire_id", questionnaireId);
                    httpClientHelper.saveAnswer(formParamsForAnswer);
                    if (questionIndexPointer == allQuestions.size() - 2) {//if next question is last one, reset the button text to"finish"
                        mainButton.setText("finish");
                    } else if (questionIndexPointer == allQuestions.size() - 1) {//if it is the last question, after saving answer, close the swing app totally
                        System.exit(0);//if I use System.exit, the whole swing app will be closed, include every related process
                        //dispose();//if I use use dispose, code below this will continue to execute which may throw IndexOutOfBoundsException
                    }
                    windowsHeight = 100;//reset height
                    questionIndexPointer++;
                    //reset questionText
                    initializeTextPanel();
                    //reset img
                    initializeImgPanel();
                    //reset options
                    initializeOptionJList();
                    setSize(300, windowsHeight);//reset windows height


                }
            }
        });
    }

    private void initializeTextPanel() {
        questionText.setText(allQuestions.get(questionIndexPointer).getQuestionText() +
                " (Choose at most " + allQuestions.get(questionIndexPointer).getQuestionOptionalOptionNum() +
                " options, " + (allQuestions.size() - questionIndexPointer - 1) + " question(s) left)");
        questionText.setEditable(false);//set question text
    }

    private void initializeImgPanel() {
        if (allQuestions.get(questionIndexPointer).getQuestionImgUrl() != null) {
            imgURL = "<html><img src='" + allQuestions.get(questionIndexPointer).getQuestionImgUrl() + "' height='180' width='300' /></html>";
            imgLabel.setText(imgURL);
            middleBTopPanel.add(imgLabel);
            middleBTopPanel.setPreferredSize(new Dimension(300, 180));
            windowsHeight += 180;
            //imgPanel=new ImagePanel(allQuestions.get(questionIndexPointer).getQuestionImgUrl());
            //middleBTopPanel's layout need to be another layout except for grid layout manager(Inteillij)
            //otherwise when adding new component into this panel will cause null pointer exception
            //middleBTopPanel.add(imgPanel);
            //middleBTopPanel.setPreferredSize(new Dimension(300,150));
        } else {
            middleBTopPanel.setPreferredSize(new Dimension(-1, -1));
        }
    }

    private void initializeOptionJList() {
        listModel.clear();
        for (int i = 1; i <= allQuestions.get(questionIndexPointer).getQuestionOptions().size(); i++) {
            String optionNum = "" + i;//load the first question's options
            listModel.addElement(allQuestions.get(questionIndexPointer).getQuestionOptions().get(optionNum));
        }
        questionOptions.setModel(listModel);
        //No need to judge whether the question is with single or multiple choice, some code in listener will do the relevant thing
//        if (allQuestions.get(questionIndexPointer).getQuestionOptionalOptionNum() >= 2) {
//            questionOptions.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//            System.out.println("MULTIPLE_INTERVAL_SELECTION");
//        } else {
//            questionOptions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//            System.out.println("SINGLE_SELECTION");
//        }
        questionOptions.setBorder(new LineBorder(Color.GRAY));
        windowsHeight += (40 * allQuestions.get(questionIndexPointer).getQuestionOptions().size());
    }


}
