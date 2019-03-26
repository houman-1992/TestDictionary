package com.houman.longman.Models.xmlModle;

import com.houman.longman.Models.xmlModle.Head.Head;
import com.houman.longman.Models.xmlModle.Sense.Sense;
import com.houman.longman.Models.xmlModle.Sense.SpokenSect;

import java.util.List;

public class Entery
    {
    private Head head;
    private List<Sense> sense;
    private SpokenSect mSpokenSect;

    public Entery(Head head, List<Sense> sense, SpokenSect mSpokenSect)
        {
        this.head = head;
        this.sense = sense;
        this.mSpokenSect = mSpokenSect;
        }

    public SpokenSect getmSpokenSect()
        {
        return mSpokenSect;
        }

    public void setmSpokenSect(SpokenSect mSpokenSect)
        {
        this.mSpokenSect = mSpokenSect;
        }

    public Head getHead()
        {
        return head;
        }

    public void setHead(Head head)
        {
        this.head = head;
        }

    public List<Sense> getSense()
        {
        return sense;
        }

    public void setSense(List<Sense> sense)
        {
        this.sense = sense;
        }
    }
