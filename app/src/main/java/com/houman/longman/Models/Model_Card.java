package com.houman.longman.Models;

public class Model_Card
    {
    private String Word;
    private String Mean;
    private String Date;
    private String Comment;
    private int Step;

    public Model_Card(String word, String mean, String date, String comment, int step)
        {
        Word = word;
        Mean = mean;
        Date = date;
        Comment = comment;
        Step = step;
        }

    public String getWord()
        {
        return Word;
        }

    public void setWord(String word)
        {
        Word = word;
        }

    public String getMean()
        {
        return Mean;
        }

    public void setMean(String mean)
        {
        Mean = mean;
        }

    public String getDate()
        {
        return Date;
        }

    public void setDate(String date)
        {
        Date = date;
        }

    public String getComment()
        {
        return Comment;
        }

    public void setComment(String comment)
        {
        Comment = comment;
        }

    public int getStep()
        {
        return Step;
        }

    public void setStep(int step)
        {
        Step = step;
        }
    }
