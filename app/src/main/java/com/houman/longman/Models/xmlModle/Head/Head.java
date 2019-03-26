package com.houman.longman.Models.xmlModle.Head;

import java.util.ArrayList;
import java.util.List;

public class Head
    {
    private String HWD;
    private String HYPHENATION;
    private String HOMNUM;
    private String PronCodes;
    private LEVEL LEVELs;
    private List<String> FRQ = new ArrayList<>();
    private List<String> SearchInflections = new ArrayList<>();

    public Head(String HWD, String HYPHENATION, String HOMNUM, String pronCodes, LEVEL LEVELs, List<String> FRQ, List<String> searchInflections)
        {
        this.HWD = HWD;
        this.HYPHENATION = HYPHENATION;
        this.HOMNUM = HOMNUM;
        PronCodes = pronCodes;
        this.LEVELs = LEVELs;
        this.FRQ = FRQ;
        SearchInflections = searchInflections;
        }

    public String getHWD()
        {
        return HWD;
        }

    public void setHWD(String HWD)
        {
        this.HWD = HWD;
        }

    public String getHYPHENATION()
        {
        return HYPHENATION;
        }

    public void setHYPHENATION(String HYPHENATION)
        {
        this.HYPHENATION = HYPHENATION;
        }

    public String getHOMNUM()
        {
        return HOMNUM;
        }

    public void setHOMNUM(String HOMNUM)
        {
        this.HOMNUM = HOMNUM;
        }

    public String getPronCodes()
        {
        return PronCodes;
        }

    public void setPronCodes(String pronCodes)
        {
        PronCodes = pronCodes;
        }

    public LEVEL getLEVELs()
        {
        return LEVELs;
        }

    public void setLEVELs(LEVEL LEVELs)
        {
        this.LEVELs = LEVELs;
        }

    public List<String> getFRQ()
        {
        return FRQ;
        }

    public void setFRQ(List<String> FRQ)
        {
        this.FRQ = FRQ;
        }

    public List<String> getSearchInflections()
        {
        return SearchInflections;
        }

    public void setSearchInflections(List<String> searchInflections)
        {
        SearchInflections = searchInflections;
        }
    }
