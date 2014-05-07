/********************************************************************************
*                                                                           	*
*   IMPORTANT: READ BEFORE DOWNLOADING, COPYING, INSTALLING OR USING.       	*
*                                                                           	*
*   By downloading, copying, installing or using the software you agree to this *
*   license. If you do not agree to this license, do not download, install,    	*
*   copy or use the software.                                      				*
*                                                                           	*
*                           	License Agreement                              	*
*          			For Vision Open Statistical Model 2D Explorer               *
*                                                                           	*
*   Copyright (C):      2010~2012 by JIA Pei, all rights reserved.          	*
*                                                                           	*
*   VOSM2DExplorer is free software under the terms of the GNU Lesser General	*
*   Public License (GNU LGPL) as published by the Free Software Foundation; 	*
*   either version 3.0 of the License, or (at your option) any later version.  	*
*   You can use it, modify it, redistribute it, etc; and redistribution and use	*
*   in source and binary forms, with or without modification, are permitted    	*
*   provided that the following conditions are met:								*
*                                                                           	*
*   a) Redistribution's of source code must retain this whole paragraph of  	*
*   copyright notice, including this list of conditions and all the following   *
*   contents in this  copyright paragraph.                        				*
*                                                                           	*
*   b) Redistribution's in binary form must reproduce this whole paragraph of 	*
*   copyright notice, including this list of conditions and all the following 	*
*   contents in this copyright paragraph, and/or other materials provided with 	*
*   the distribution.                                         					*
*                                                                           	*
*   c) The name of the copyright holders may not be used to endorse or promote 	*
*   products derived from this software without specific prior written      	*
*   permission.                                                     			*
*                                                                           	*
*   Any publications based on this code must cite the following 2 papers,		*
*   technical reports and on-line materials.                                	*
*   1) P. JIA, 2D Statistical Models, Technical Report of Vision Open Working  	*
*   Group, 2st Edition, October 21, 2010.                           			*
*   http://www.visionopen.com/members/jiapei/publications/pei_sm2dreport2010.pdf*
*   2) P. JIA. Audio-visual based HMI for an Intelligent Wheelchair.        	*
*   PhD thesis, University of Essex, February, 2011.                        	*
*   http://www.visionopen.com/members/jiapei/publications/pei_phdthesis2010.pdf	*
*                                                                           	*
* Version:          1.0                                                     	*
* Author:           JIA Pei                                                 	*
* Contact:          jp4work@gmail.com                                       	*
* URL:              http://www.visionopen.com                               	*
* Modified Date:    2012-05-22                                              	*
********************************************************************************/
package com.visionopen.jiapei.sm2d;


import java.io.*;

import Jama.Matrix;

import com.visionopen.jiapei.smbasic.VO_Shape;
import com.visionopen.jiapei.smbasic.VO_Texture;
import com.visionopen.jiapei.utils.PCA;




public class VO_AppearanceModel extends VO_TextureModel
{
	/** PCA transform for appearance, including eigenvectors, eigenvalues, and mean */
	PCA									m_PCAAppearance;

    /** Shape weights, for scaling to texture scale. 21*21 */
	protected float[][]             	m_MatWeightsScaleShape2Texture;

	/** Original appearance project to truncated space. For IMM, 240*18 */
	protected float[][]             	m_MatAppearanceProject2Truncated;

    /** The shape part of the appearance model eigenvectors, 18*21 */
	protected float[][]             	m_MatPcs;

    /** The texture part of the appearance model eigenvectors, 18*127 */
	protected float[][]             	m_MatPcg;

    /** For shape, 116*18, refer to Cootes "Comparing Variations on the Active Appearance Model Algorithm" */
	protected float[][]             	m_MatQs;

    /** For texture, 90396*18, refer to Cootes "Comparing Variations on the Active Appearance Model Algorithm" */
	protected float[][]             	m_MatQg;

    /** In face, m_MatRc and m_MatRt are just the Hessian Matrics!!! */
    /** For shape, 18*90396, Multivariate Linear Regression Matrix, refer to Stegmann's AAM-API Equation (7.4) */
	protected float[][]             	m_MatRc;

    /** For shape, 4*90396, Multivariate Linear Regression Matrix, refer to Stegmann's AAM-API Equation (7.4) */
    /** JIA Pei declare that this variable is absolutely useless, and if using it, it makes everything unreasonable and illogical */
	protected float[][]             	m_MatRt;
	
    /** Totally, n=m(4(k+4))=240*(4*(18+4))=21120 displacements. 4*18 */
    float[][] 	    					m_vvCDisps;

    /** Totally, n=m(4(k+4))=240*(4*(18+4))=21120 displacements. 4*4, refer to AAM-API page 3 of 10 */
    float[][] 	    					m_vvPoseDisps;

    /** Stegmann: Gradient Matrix 90396*18 */
    float[][]                    		m_MatCParamGradientMatrix;

    /** Stegmann: Pose Gradient Matrix 90396*4 we may ignore this */
    float[][]                   		m_MatPoseGradientMatrix;

    /** Number of Appearance m_iNbOfAppearance = m_iNbOfShapeEigens + m_iNbOfTextureEigens. For IMM: 21+127=148 */
	protected int                   	m_iNbOfAppearance;

	/** Most possible appearance model eigens before PCA. For IMM: min (148, 240) = 148 */
	protected int                		m_iNbOfEigenAppearanceAtMost;

    /** Number of appearance model eigens. For IMM: 18 */
	protected int                   	m_iNbOfAppearanceEigens;

    /** Truncate Percentage for appearance PCA. Normally, 0.95 */
	protected float                 	m_fTruncatedPercent_Appearance;

	protected void init() {
		m_iNbOfAppearance					= 0;
		m_iNbOfAppearanceEigens				= 0;
		m_iNbOfEigenAppearanceAtMost		= 0;
		m_fTruncatedPercent_Appearance		= 0.95f;
		
		m_PCAAppearance						= null;
		m_MatWeightsScaleShape2Texture		= null;
		m_MatAppearanceProject2Truncated	= null;
		m_MatPcs							= null;
		m_MatPcg							= null;
		m_MatQs								= null;
		m_MatQg								= null;
		m_MatRc								= null;
		m_MatRt								= null;
	}

	public VO_AppearanceModel() {
		init();
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////Regression//////////////////////////////////////////////
	/** Unfinished yet.... */
    /** Off-line build prediction matrix for fitting. */
    /** Please refer to http://www2.imm.dtu.dk/~aam/main/node16.html; revised from AAM-API */
    public void                         VO_CalcRegressionMatrices()
    {
    	
    }

    /** Carry out C displacement experiments */
    public void                         VO_DoCParamExperiments()
    {
    	
    }

    /** Carry out pose displacement experiments */
    public void                         VO_DoPoseExperiments()
    {
    	
    }

    /** Carry out multi variate linear regression experiments */
    public void                         VO_DoRegression()
    {
    	
    }
	////////////////////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////////////////////
	//////Gradient -- This is what's happening in Stegmann's code///////////////////////////////
    /** Build gradient matrices */
    public void                         VO_CalcGradientMatrices()
    {
    	
    }

    /** Build gradient matrices in terms of C parameters */
    public void                         VO_EstCParamGradientMatrix(float[][] oCParamGM)
    {
    	
    }

    /** Build gradient matrices in terms of pose */
    public void                         VO_EstPoseGradientMatrix(float[][] oPoseGM)
    {
    	
    }
	////////////////////////////////////////////////////////////////////////////////////////////
    
	/** Appearance parameters constraints */
	public void							VO_AppearanceParameterConstraint(float[] ioC, float nSigma)
	{
    	int NbOfAppearanceParameterConstraints = ioC.length;
    	float ct = 0.0f;

        for (int i = 0; i < NbOfAppearanceParameterConstraints; ++i)
        {
        	ct = nSigma * (float) Math.sqrt(this.m_PCAAppearance.eigenvalues[i]);
            if ( ioC[i] > ct )
            {
            	ioC[i] = ct;
            }
            else if ( ioC[i] < -ct )
            {
            	ioC[i] = -ct;
            }
        }
	}

	/** Shape and texture project to shape parameters and texture parameters, and then concatenated */
    public void							VO_ShapeTexture2Appearance( final VO_Shape iShape, final VO_Texture iTexture, float[] b )
    {
    	float s = this.m_MatWeightsScaleShape2Texture[0][0];
        float[] b_s = this.m_PCAAlignedShape.project( iShape.GetTheShapeInARow() );			// 1*116->1*21
        float[] b_t = this.m_PCANormalizedTexture.project(iTexture.GetTheTextureInARow());	// 1*90396->1*127
		b = new float[this.m_iNbOfShapeEigens + this.m_iNbOfTextureEigens];
		
        for(int i = 0; i < this.m_iNbOfShapeEigens; i++)
        {
        	b[i] = b_s[i]*s;
        }
        for(int i = 0; i < this.m_iNbOfTextureEigens; i++)
        {
        	b[this.m_iNbOfShapeEigens+i] = b_t[i];
        }
    }
    
    /** Appearance projected to appearance parameters */
    public void   	                	VO_AppearanceProjectToCParam(final float[] app, float[] outC)
    {
    	this.m_PCAAppearance.project(app, outC);
    }
    
    /** Shape parameters and texture parameters projected to concatenated parameters */
    public void 						VO_SParamTParamProjectToCParam(final float[] inS, final float[] inT, float[] outC)
    {
    	float[] tempConcatenated = new float[this.m_iNbOfShapeEigens + this.m_iNbOfTextureEigens];
    	float s = this.m_MatWeightsScaleShape2Texture[0][0];
        for(int i = 0; i < this.m_iNbOfShapeEigens; i++)
        {
        	tempConcatenated[i] = inS[i]*s;
        }
        for(int i = 0; i < this.m_iNbOfTextureEigens; i++)
        {
        	tempConcatenated[this.m_iNbOfShapeEigens+i] = inT[i];
        }

        float[] tmp = this.m_PCAAppearance.project(tempConcatenated);
		java.lang.System.arraycopy(tmp, 0, outC, 0, tmp.length);
    }
    
    /** Appearance parameters back projected to appearance */
    public void   	                	VO_CParamBackProjectToAppearance(final float[] inC, float[] app)
    {
    	this.m_PCAAppearance.backProject(inC, app);
    }
    
    /** Concatenated parameters back projected to shape parameters and texture parameters */
    public void                         VO_CParamBackProjectToSParamTParam(final float[] inC, float[] outS, float[] outT)
    {
        float[] tempConcatenated = this.m_PCAAppearance.backProject(inC);
//        outS = new float[this.m_iNbOfShapeEigens];
//        outT = new float[this.m_iNbOfTextureEigens];
        
        float s = 1.0f/this.m_MatWeightsScaleShape2Texture[0][0];
        for(int i = 0; i < this.m_iNbOfShapeEigens; i++)
        {
        	outS[i] = tempConcatenated[i]*s;
        }
        for(int i = 0; i < this.m_iNbOfTextureEigens; i++)
        {
        	outT[i] = tempConcatenated[this.m_iNbOfShapeEigens+i];
        }
    }
    
    /** Concatenated parameters back projected to aligned shape */
    public void                         VO_CParamBackProjectToAlignedShape(final float[] inC, float[] oShape)
    {
    	double[] inCd = com.visionopen.jiapei.utils.Common.float2double(inC);
    	double[][] m_MatQsd = com.visionopen.jiapei.utils.Common.float2double(this.m_MatQs);
    	double[] meand = com.visionopen.jiapei.utils.Common.float2double(this.m_PCAAlignedShape.mean);
    	
    	Matrix A = new Matrix( inCd, 1);
		Matrix B = new Matrix( m_MatQsd );
		Matrix C = new Matrix( meand, 1);
		double[] D = new double [meand.length];
		D = (A.times(B.transpose()).plus(C)).getRowPackedCopy();
    	oShape = com.visionopen.jiapei.utils.Common.double2float(D);
    }

    /** Concatenated parameters back projected to normalized texture */
    public void                         VO_CParamBackProjectToNormalizedTexture(final float[] inC, float[] oTexture)
    {
    	double[] inCd = com.visionopen.jiapei.utils.Common.float2double(inC);
    	double[][] m_MatQgd = com.visionopen.jiapei.utils.Common.float2double(this.m_MatQg);
    	double[] meand = com.visionopen.jiapei.utils.Common.float2double(this.m_PCAAlignedShape.mean);
    	
    	Matrix A = new Matrix( inCd, 1);
		Matrix B = new Matrix( m_MatQgd );
		Matrix C = new Matrix( meand, 1);
		double[] D = new double [meand.length];
		D = (A.times(B.transpose()).plus(C)).getRowPackedCopy();
		oTexture = com.visionopen.jiapei.utils.Common.double2float(D);
    }

    /** Build displacement sets */
    public void                         VO_CreateDisplacementSets()
    {
    	
    }

    /** Build displacement sets for C parameters */
    public static float[][]   			VO_CalcCParamDisplacementVectors(final float[] vStdDisp, final float[] cVectors)
    {
    	float[][] res = new float[1][1];
    	
    	return res;
    }

    /** Build displacement sets for Pose parameters */
    public static float[][]   			VO_CalcPoseDisplacementVectors( final float[] vScaleDisp, final float[] vRotDisp, final float[] vXDisp, final float[] vYDisp, int method)
    {
    	float[][] res = new float[1][1];
    	
    	return res;
    }

	/** Build Appearance model */
    public void                         VO_BuildAppearanceModel(final String[] allLandmarkFiles4Training,
															final String[] allImgFiles4Training,
															final String shapeinfoFileName,
															int database,
															int levels,
															int channels,
															int trm,
															float TPShape,
															float TPTexture,
															float TPConcatenated,
															boolean useKnownTriangles)
    {
    	
    }

    /** Save Appearance Model, to a specified folder */
    public void                         VO_Save(final String fd)
    {
    	
    }

    /** Load all parameters */
    public void                         VO_LoadOthers(final String fd)
	{
    	if(fd != "")
		{
    		super.VO_LoadOthers(fd);
		}
	}

    /** Load Parameters for fitting */
	public void                         VO_LoadParameters4Fitting(final String fd)
	{
		if(fd != "")
		{
			init();
			super.VO_LoadParameters4Fitting(fd);
			String folderDir = fd+"/AppearanceModel";
		    String filename = new String();
		    FileReader filereader = null;
		    BufferedReader in = null;
		    String str = new String();
		    String ss[] = null;
		    
		    // Read AppearanceModel
			try {
				filename = folderDir + "/AppearanceModel.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
	    
		        str = in.readLine();
		        str = in.readLine();
		        this.m_iNbOfAppearance = Integer.parseInt(str);
		        
		        str = in.readLine();
		        str = in.readLine();
		        this.m_iNbOfEigenAppearanceAtMost = Integer.parseInt(str);
		        
		        str = in.readLine();
		        str = in.readLine();
		        this.m_iNbOfAppearanceEigens = Integer.parseInt(str);
		        
		        str = in.readLine();
		        str = in.readLine();
		        this.m_fTruncatedPercent_Appearance = Float.parseFloat(str);
		        
		        in.close();
		        filereader.close();
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
		    
		    this.m_PCAAppearance = new PCA();
		    
		    // m_PCAAppearanceMean
			try {
				filename = folderDir + "/m_PCAAppearanceMean.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_PCAAppearance.mean = new float[this.m_iNbOfAppearance];
		        
		        if ( (str = in.readLine())!=null)
		        {
		        	ss = str.split(" ");
		        	assert (this.m_iNbOfAppearance == ss.length);
		        	for(int i = 0; i < this.m_iNbOfAppearance; i++)
		        	{
		        		this.m_PCAAppearance.mean[i] = Float.parseFloat(ss[i]);
		        	}
		        }
		        
		        in.close();
		        filereader.close();
		        
			} catch (IOException e){
				e.printStackTrace();
			}

			// m_PCAAppearanceEigenValues
			try {
				filename = folderDir + "/m_PCAAppearanceEigenValues.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_PCAAppearance.eigenvalues = new float[this.m_iNbOfAppearanceEigens];
		        
	        	for(int i = 0; i < this.m_iNbOfAppearanceEigens; i++)
	        	{
	        		if ( (str = in.readLine())!=null)
	        			this.m_PCAAppearance.eigenvalues[i] = Float.parseFloat(str);
	        	}
		        
		        in.close();
		        filereader.close();
		        
			} catch (IOException e){
				e.printStackTrace();
			}
			
			// m_PCAAppearanceEigenVectors
			try {
				filename = folderDir + "/m_PCAAppearanceEigenVectors.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_PCAAppearance.eigenvectors = new float[this.m_iNbOfAppearanceEigens][this.m_iNbOfAppearance];

		        for(int i = 0; i < this.m_iNbOfAppearanceEigens; i++)
		        {
		        	if ( (str = in.readLine())!=null)
			        	ss = str.split(" ");
	        		assert (this.m_iNbOfAppearance == ss.length);
		        	for(int j = 0; j < this.m_iNbOfAppearance; j++)
		        	{
		        		this.m_PCAAppearance.eigenvectors[i][j] = Float.parseFloat(ss[j]);
		        	}
		        }
		        
		        in.close();
		        filereader.close();
		        
			} catch (IOException e){
				e.printStackTrace();
			}
			
			// m_MatWeightsScaleShape2Texture
			try {
				filename = folderDir + "/m_MatWeightsScaleShape2Texture.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_MatWeightsScaleShape2Texture = new float[this.m_iNbOfAppearanceEigens][this.m_iNbOfAppearanceEigens];

		        for(int i = 0; i < this.m_iNbOfAppearanceEigens; i++)
		        {
		        	if ( (str = in.readLine())!=null)
			        	ss = str.split(" ");
	        		assert (this.m_iNbOfAppearanceEigens == ss.length);
		        	for(int j = 0; j < this.m_iNbOfAppearanceEigens; j++)
		        	{
		        		this.m_MatWeightsScaleShape2Texture[i][j] = Float.parseFloat(ss[j]);
		        	}
		        }
		        
		        in.close();
		        filereader.close();
		        
			} catch (IOException e){
				e.printStackTrace();
			}

		    // m_MatPcs	
			try {
				filename = folderDir + "/m_MatPcs.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_MatPcs = new float[this.m_iNbOfAppearanceEigens][this.m_iNbOfShapeEigens];

		        for(int i = 0; i < this.m_iNbOfAppearanceEigens; i++)
		        {
		        	if ( (str = in.readLine())!=null)
			        	ss = str.split(" ");
	        		assert (this.m_iNbOfShapeEigens == ss.length);
		        	for(int j = 0; j < this.m_iNbOfShapeEigens; j++)
		        	{
		        		this.m_MatPcs[i][j] = Float.parseFloat(ss[j]);
		        	}
		        }
		        
		        in.close();
		        filereader.close();
		        
			} catch (IOException e){
				e.printStackTrace();
			}

		    // m_MatPcg
			try {
				filename = folderDir + "/m_MatPcg.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_MatPcg = new float[this.m_iNbOfAppearanceEigens][this.m_iNbOfTextureEigens];

		        for(int i = 0; i < this.m_iNbOfAppearanceEigens; i++)
		        {
		        	if ( (str = in.readLine())!=null)
			        	ss = str.split(" ");
	        		assert (this.m_iNbOfTextureEigens == ss.length);
		        	for(int j = 0; j < this.m_iNbOfTextureEigens; j++)
		        	{
		        		this.m_MatPcg[i][j] = Float.parseFloat(ss[j]);
		        	}
		        }
		        
		        in.close();
		        filereader.close();
		        
			} catch (IOException e){
				e.printStackTrace();
			}

		    // m_MatQs
			try {
				filename = folderDir + "/m_MatQs.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_MatQs = new float[this.m_iNbOfShapes][this.m_iNbOfAppearanceEigens];

		        for(int i = 0; i < this.m_iNbOfShapes; i++)
		        {
		        	if ( (str = in.readLine())!=null)
			        	ss = str.split(" ");
	        		assert (this.m_iNbOfAppearanceEigens == ss.length);
		        	for(int j = 0; j < this.m_iNbOfAppearanceEigens; j++)
		        	{
		        		this.m_MatQs[i][j] = Float.parseFloat(ss[j]);
		        	}
		        }
		        
		        in.close();
		        filereader.close();
		        
			} catch (IOException e){
				e.printStackTrace();
			}

		    // m_MatQg
			try {
				filename = folderDir + "/m_MatQg.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_MatQg = new float[this.m_iNbOfTextures][this.m_iNbOfAppearanceEigens];

		        for(int i = 0; i < this.m_iNbOfTextures; i++)
		        {
		        	if ( (str = in.readLine())!=null)
			        	ss = str.split(" ");
	        		assert (this.m_iNbOfAppearanceEigens == ss.length);
		        	for(int j = 0; j < this.m_iNbOfAppearanceEigens; j++)
		        	{
		        		this.m_MatQg[i][j] = Float.parseFloat(ss[j]);
		        	}
		        }
		        
		        in.close();
		        filereader.close();
		        
			} catch (IOException e){
				e.printStackTrace();
			}

		    // m_MatRc
			try {
				filename = folderDir + "/m_MatRc.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_MatRc = new float[this.m_iNbOfAppearanceEigens][this.m_iNbOfTextures];

		        for(int i = 0; i < this.m_iNbOfAppearanceEigens; i++)
		        {
		        	if ( (str = in.readLine())!=null)
			        	ss = str.split(" ");
	        		assert (this.m_iNbOfTextures == ss.length);
		        	for(int j = 0; j < this.m_iNbOfTextures; j++)
		        	{
		        		this.m_MatRc[i][j] = Float.parseFloat(ss[j]);
		        	}
		        }
		        
		        in.close();
		        filereader.close();
		        
			} catch (IOException e){
				e.printStackTrace();
			}

		    // m_MatRt
			try {
				filename = folderDir + "/m_MatRt.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_MatRt = new float[4][this.m_iNbOfTextures];

		        for(int i = 0; i < 4; i++)
		        {
		        	if ( (str = in.readLine())!=null)
			        	ss = str.split(" ");
	        		assert (this.m_iNbOfTextures == ss.length);
		        	for(int j = 0; j < this.m_iNbOfTextures; j++)
		        	{
		        		this.m_MatRt[i][j] = Float.parseFloat(ss[j]);
		        	}
		        }
		        
		        in.close();
		        filereader.close();
		        
			} catch (IOException e){
				e.printStackTrace();
			}

		    // m_MatCParamGradientMatrix
			try {
				filename = folderDir + "/m_MatCParamGradientMatrix.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_MatCParamGradientMatrix = new float[this.m_iNbOfTextures][this.m_iNbOfAppearanceEigens];

		        for(int i = 0; i < this.m_iNbOfTextures; i++)
		        {
		        	if ( (str = in.readLine())!=null)
			        	ss = str.split(" ");
	        		assert (this.m_iNbOfAppearanceEigens == ss.length);
		        	for(int j = 0; j < this.m_iNbOfAppearanceEigens; j++)
		        	{
		        		this.m_MatCParamGradientMatrix[i][j] = Float.parseFloat(ss[j]);
		        	}
		        }
		        
		        in.close();
		        filereader.close();
		        
			} catch (IOException e){
				e.printStackTrace();
			}

		    // m_MatPoseGradientMatrix, we may ignore this first
			try {
				filename = folderDir + "/m_MatPoseGradientMatrix.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_MatPoseGradientMatrix = new float[this.m_iNbOfTextures][4];

		        for(int i = 0; i < this.m_iNbOfTextures; i++)
		        {
		        	if ( (str = in.readLine())!=null)
			        	ss = str.split(" ");
	        		assert (4 == ss.length);
		        	for(int j = 0; j < 4; j++)
		        	{
		        		this.m_MatPoseGradientMatrix[i][j] = Float.parseFloat(ss[j]);
		        	}
		        }
		        
		        in.close();
		        filereader.close();
		        
			} catch (IOException e){
				e.printStackTrace();
			}
		    
		}
	}

    /** Gets and Sets */
	public float[]                 		GetAppearanceMean() {return this.m_PCAAppearance.mean;}
	public float[]                 		GetAppearanceEigenValues() {return this.m_PCAAppearance.eigenvalues;}
	public float[][]                 	GetAppearanceEigenVectors() {return this.m_PCAAppearance.eigenvectors;}
	public float[][]                  	GetWeightsScaleShape2Texture() {return this.m_MatWeightsScaleShape2Texture;}
	public float[][]					GetAppearanceProject2Truncated() {return this.m_MatAppearanceProject2Truncated;}
	public float[][]                    GetPcs() {return this.m_MatPcs;}
	public float[][]                    GetPcg() {return this.m_MatPcg;}
	public float[][]                    GetQs() {return this.m_MatQs;}
	public float[][]                    GetQg() {return this.m_MatQg;}
	public float[][]                    GetRc() {return this.m_MatRc;}
	public float[][]                    GetRt() {return this.m_MatRt;}
	public float[][]          			GetCDisps() {return this.m_vvCDisps;}
	public float[][]          			GetPoseDisps() {return this.m_vvPoseDisps;}
	public float[][]                  	GetCParamGradientMatrix() {return this.m_MatCParamGradientMatrix;}
	public float[][]                  	GetPoseGradientMatrix() {return this.m_MatPoseGradientMatrix;}
	public int                    		GetNbOfAppearance() {return this.m_iNbOfAppearance;}
	public int                    		GetNbOfEigenAppearanceAtMost() {return this.m_iNbOfEigenAppearanceAtMost;}
	public int                    		GetNbOfAppearanceEigens() {return this.m_iNbOfAppearanceEigens;}
	public float                        GetTruncatedPercent_Concatenated() {return this.m_fTruncatedPercent_Appearance;}

    public static void main(String args[]) {
    	String fd = "/home/jiapei/MyPrograms/Eclipse/AAMJava3D/traineddata/2D/JIAPei";
    	VO_AppearanceModel appearancemodel = new VO_AppearanceModel();
    	appearancemodel.VO_LoadParameters4Fitting(fd);
    	appearancemodel.VO_LoadOthers(fd);
	}
}
