package com.demo.model;

public class Testscore {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column testscore.student_id
     *
     * @mbg.generated
     */
    private Integer studentId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column testscore.subject
     *
     * @mbg.generated
     */
    private String subject;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column testscore.score
     *
     * @mbg.generated
     */
    private Integer score;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column testscore.student_id
     *
     * @return the value of testscore.student_id
     *
     * @mbg.generated
     */
    public Integer getStudentId() {
        return studentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column testscore.student_id
     *
     * @param studentId the value for testscore.student_id
     *
     * @mbg.generated
     */
    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column testscore.subject
     *
     * @return the value of testscore.subject
     *
     * @mbg.generated
     */
    public String getSubject() {
        return subject;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column testscore.subject
     *
     * @param subject the value for testscore.subject
     *
     * @mbg.generated
     */
    public void setSubject(String subject) {
        this.subject = subject == null ? null : subject.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column testscore.score
     *
     * @return the value of testscore.score
     *
     * @mbg.generated
     */
    public Integer getScore() {
        return score;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column testscore.score
     *
     * @param score the value for testscore.score
     *
     * @mbg.generated
     */
    public void setScore(Integer score) {
        this.score = score;
    }

}