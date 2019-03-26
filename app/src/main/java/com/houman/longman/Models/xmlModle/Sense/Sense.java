package com.houman.longman.Models.xmlModle.Sense;

public class Sense
    {
    private String id;
    private String DEF;
    private ExpandableInformation ExpandableInformations;
    private int SP;

    public Sense(String id, String DEF, ExpandableInformation expandableInformations, int SP)
        {
        this.id = id;
        this.DEF = DEF;
        ExpandableInformations = expandableInformations;
        this.SP = SP;
        }

    public String getId()
        {
        return id;
        }

    public void setId(String id)
        {
        this.id = id;
        }

    public String getDEF()
        {
        return DEF;
        }

    public void setDEF(String DEF)
        {
        this.DEF = DEF;
        }

    public ExpandableInformation getExpandableInformations()
        {
        return ExpandableInformations;
        }

    public void setExpandableInformations(ExpandableInformation expandableInformations)
        {
        ExpandableInformations = expandableInformations;
        }

    public int getSP()
        {
        return SP;
        }

    public void setSP(int SP)
        {
        this.SP = SP;
        }
    }
