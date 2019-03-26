package com.houman.longman.Models;

public class mMemorize
    {
    private String Word;
    private String Date;
    private int Step;
    private String Cate;

    public mMemorize(String word, String date, int step, String cate)
        {
        Word = word;
        Date = date;
        Step = step;
        Cate = cate;
        }

    public String getWord()
        {
        return Word;
        }

    public void setWord(String word)
        {
        Word = word;
        }

    public String getDate()
        {
        return Date;
        }

    public void setDate(String date)
        {
        Date = date;
        }

    public int getStep()
        {
        return Step;
        }

    public void setStep(int step)
        {
        Step = step;
        }

    public String getCate()
        {
        return Cate;
        }

    public void setCate(String cate)
        {
        Cate = cate;
        }
    }
