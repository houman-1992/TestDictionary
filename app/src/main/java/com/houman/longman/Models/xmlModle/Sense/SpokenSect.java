package com.houman.longman.Models.xmlModle.Sense;

import java.util.ArrayList;
import java.util.List;

public class SpokenSect
    {
    private List<Sense> SpSense = new ArrayList<>();

    public SpokenSect(List<Sense> spSense)
        {
        SpSense = spSense;
        }

    public List<Sense> getSpSense()
        {
        return SpSense;
        }

    public void setSpSense(List<Sense> spSense)
        {
        SpSense = spSense;
        }
    }
