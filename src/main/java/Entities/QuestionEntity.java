package Entities;

import java.util.Map;

public class QuestionEntity {
    private Long questionId;

    private Long questionnaireId;

    private String questionText;

    private String questionImgUrl;

    private Integer questionOptionalOptionNum;

    private Map<String, String> questionOptions;

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(Long questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionImgUrl() {
        return questionImgUrl;
    }

    public void setQuestionImgUrl(String questionImgUrl) {
        this.questionImgUrl = questionImgUrl;
    }

    public Integer getQuestionOptionalOptionNum() {
        return questionOptionalOptionNum;
    }

    public void setQuestionOptionalOptionNum(Integer questionOptionalOptionNum) {
        this.questionOptionalOptionNum = questionOptionalOptionNum;
    }

    public Map<String, String> getQuestionOptions() {
        return questionOptions;
    }

    public void setQuestionOptions(Map<String, String> questionOptions) {
        this.questionOptions = questionOptions;
    }
}
