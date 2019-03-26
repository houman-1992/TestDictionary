package com.houman.longman.Models;

public class MainModel
    {
    private String HWD;
    private String File;

    public MainModel(String HWD, String file)
        {
        this.HWD = HWD;
        File = file;
        }

    public String getHWD()
        {
        return HWD;
        }

    public void setHWD(String HWD)
        {
        this.HWD = HWD;
        }

    public String getFile()
        {
        return File;
        }

    public void setFile(String file)
        {
        File = file;
        }

    }
