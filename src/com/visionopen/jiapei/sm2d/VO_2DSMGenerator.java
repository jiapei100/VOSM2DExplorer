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
* Copyright (C):    2006~2014 by JIA Pei, all rights reserved.                                      *
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
* Version:          1.0                                                                             *
* Author:           JIA Pei                                                                         *
* Contact:          jp4work@gmail.com                                                               *
* URL:              http://www.visionopen.com                                                       *
* Create Date:      2010-12-25                                                                      *
* Modify Date:      2014-05-07                                                                      *
****************************************************************************************************/

package com.visionopen.jiapei.sm2d;


import java.io.*;



public class VO_2DSMGenerator {
	VO_AppearanceModel appearanceModel;
	float[] onemodel;
	
	VO_2DSMGenerator()	{}
	
	public void GenerateAllModels(final String fd)
	{
		appearanceModel = new VO_AppearanceModel();
		appearanceModel.VO_LoadParameters4Fitting(fd);
		appearanceModel.VO_LoadOthers(fd);
		onemodel = new float[appearanceModel.m_iNbOfShapeEigens+
		                      appearanceModel.m_iNbOfTextureEigens+
		                      appearanceModel.m_iNbOfAppearanceEigens];
		float[] sParams = new float[appearanceModel.m_iNbOfShapeEigens];
		float[] tParams = new float[appearanceModel.m_iNbOfTextureEigens];
		float[] cParams = new float[appearanceModel.m_iNbOfAppearanceEigens];
		
		for(int i = 0; i < appearanceModel.m_iNbOfSamples; i++)
		{
			appearanceModel.VO_AlignedShapeProjectToSParam(appearanceModel.m_vAlignedShapes[i], sParams);
			appearanceModel.VO_NormalizedTextureProjectToTParam(appearanceModel.m_vNormalizedTextures[i], tParams);
			appearanceModel.VO_SParamTParamProjectToCParam(sParams, tParams, cParams);
	        for(int j = 0; j < appearanceModel.m_iNbOfShapeEigens; j++)
	        	onemodel[j] = sParams[j];
	        
	        for(int j = 0; j < appearanceModel.m_iNbOfTextureEigens; j++)
	        	onemodel[appearanceModel.m_iNbOfShapeEigens+j] = tParams[j];
	        
	        for(int j = 0; j < appearanceModel.m_iNbOfAppearanceEigens; j++)
	        	onemodel[appearanceModel.m_iNbOfShapeEigens+appearanceModel.m_iNbOfTextureEigens+j] = cParams[j];
	        
	        String tt = fd+i+".mod";
	        
    		try {
    			FileWriter fwfn = new FileWriter(tt);
    	        BufferedWriter out = new BufferedWriter(fwfn);
    	        
    	        for(int j = 0; j < onemodel.length; j++)
    	        {
    	        	out.write(onemodel[j] + " ");
    	        }
    	        
    	        out.close();
    	        fwfn.close();
    	        	
    		} catch (IOException e){
    			e.printStackTrace();
    		}
		}
	}
	
	public static void main(String[] args) {
		VO_2DSMGenerator ge = new VO_2DSMGenerator();
		//ge.GenerateAllModels(args[0]);
		String fn = "F:\\Research\\Databases\\face\\2D\\EMOUNT\\traineddata\\60Frontal\\BGR\\";
		ge.GenerateAllModels(fn);
	}
}

