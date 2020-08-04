import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        QuestionnaireForm questionnaireForm = new QuestionnaireForm("3140407087");
        //questionnaire.pack();
        //questionnaireForm.setSize(300,400);
        questionnaireForm.setResizable(false);
        questionnaireForm.setLocationRelativeTo(null);//let the app show in the middle of the screen
        questionnaireForm.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        questionnaireForm.setVisible(true);
        questionnaireForm.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                JOptionPane.showMessageDialog(null, "You can't close the windows until you finish all of the questions!", "Warning", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
