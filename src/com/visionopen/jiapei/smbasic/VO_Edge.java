/****************************************************************************************************
*                                                                                                   *
*                   IMPORTANT: READ BEFORE DOWNLOADING, COPYING, INSTALLING OR USING.               *
*                                                                                                   *
*   By downloading, copying, installing or using the software you agree to this license.            *
*   If you do not agree to this license, do not download, install, copy or use the software.        *
*                                                                                                   *
*                                   License Agreement                                               *
*                           For Vision Open Statistical Models 2D Explorer                          *
*                                                                                                   *
* Copyright (C):    2006~2021 by JIA Pei, all rights reserved.                                      *
*                                                                                                   *
*                   VOSM2DExplorer is free software under the terms of the GNU Lesser General       *
*                   Public License (GNU LGPL) as published by the Free Software Foundation;         *
*                   either version 3.0 of the License, or (at your option) any later version.       *
*                                                                                                   *
*                   You can use it, modify it, redistribute it, etc; and redistribution and use in  *
*                   source and binary forms, with or without modification, are permitted provided   *
*                   that the following conditions are met:                                          *
*                                                                                                   *
*                   a) Redistribution's of source code must retain this whole paragraph of          *
*                   copyright notice, including this list of conditions and all the following       *
*                   contents in this copyright paragraph.                                           *
*                                                                                                   *
*                   b) Redistribution's in binary form must reproduce this whole paragraph of       *
*                   copyright notice, including this list of conditions and all the following       *
*                   contents in this copyright paragraph, and/or other materials provided with      *
*                   the distribution.                                                               *
*                                                                                                   *
*                   c) The name of the copyright holders may not be used to endorse or promote      *
*                   products derived from this software without specific prior written permission.  *
*                                                                                                   *
*                   Any publications based on this code must cite the following five papers,        *
*                   technic reports and on-line materials.                                          *
*                                                                                                   *
*                   1) P. JIA, 2D Statistical Models, Technical Report of Vision Open Working       *
*                   Group, 2st Edition, Oct 21, 2010.                                               *
*                   http://www.visionopen.com/members/jiapei/publications/pei_sm2dreport2010.pdf    *
*                                                                                                   *
*                   2) P. JIA. Audio-visual based HMI for an Intelligent Wheelchair.                *
*                   PhD thesis, University of Essex, 2010.                                          *
*                   http://www.visionopen.com/members/jiapei/publications/pei_thesischapter34.pdf   *
*                                                                                                   *
* Version:          1.1                                                                             *
* Author:           JIA Pei                                                                         *
* Contact:          jiapei@longervision.com                                                         *
* URL:              http://www.longervision.com                                                     *
* Create Date:      2010-12-25                                                                      *
* Modify Date:      2014-05-07                                                                      *
* Modify Date:      2021-05-07                                                                      *
****************************************************************************************************/

package com.visionopen.jiapei.smbasic;

public class VO_Edge {
    /** First vertex' index of this edge */
    private int index1;

    /** Second vertex' index of this edge */
    private int index2;

    /** Default constructor to create a VO_Edge object */
    public VO_Edge() {}

    /** Constructor to create a VO_Edge object with two vertexes' indexes */
    public VO_Edge(int p1, int p2)
    {
        this.index1 = p1;
        this.index2 = p2;
    }

    /** Default constructor to create a VO_Edge object */
    public boolean equals (final VO_Edge e)
    {
        if ( ( this.index1 == e.GetIndex1() && this.index2 == e.GetIndex2() ) ||
            ( this.index1 == e.GetIndex2() && this.index2 == e.GetIndex1() ) )
            return true;
        else return false;
    }

    /** Get first index of the first edge vertex */
    public int GetIndex1() { return this.index1;}

    /** Get second index of the first edge vertex */
    public int GetIndex2() { return this.index2;}

    /** Set the first index of the first edge vertex */
    public void SetIndex1(int idx1) { this.index1 = idx1;}

    /** Set the second index of the second edge vertex */
    public void SetIndex2(int idx2) { this.index2 = idx2;}
}
