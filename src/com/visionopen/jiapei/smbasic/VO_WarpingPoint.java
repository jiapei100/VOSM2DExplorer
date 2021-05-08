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

import javax.vecmath.Point2f;




public class VO_WarpingPoint {
    /** Warping point coordinates */
	private Point2f					m_CVPosition;

    /** Which triangle is this point in, in the AAM model triangle vector? */
	private int                		m_iTriangleIndex;

    /** What's the position in the AAM model template face point vector? */
	private int                		m_iPointIndex;

    /** The triangle that this warping point belong to */
	private VO_Triangle2DStructure	m_VOTriangle2DStructure;

    /** Gradients in 3 channels (blue, green, red) for this warping point */
	private float[][]	    		m_Gradients;

    /** The Jacobian - 1st part , refer to http://www.visionopen.com/cv/aam.php */
    private float[]               	m_Jacobian_One;

    /** The Jacobian Matrix in terms of shape parameters 2*20 */
    private float[][]      			m_JacobianMatrix4ShapeModel;

    /** The Jacobian Matrix in terms of global shape normalizing transform */
    private float[][]      			m_JacobianMatrix4GlobalShapeNorm;

    /** Steepest Descent Images 3 (blue, green, red) * 20 (p parameters). */
    private float[][]	    		m_SteepestDescentImages4ShapeModel;

    /** Steepest Descent Images for Global Shape Transform 3 (blue, green, red) * 4. */
    private float[][]	    		m_SteepestDescentImages4GlobalShapeNorm;

    /** Default constructor to create a VO_WarpingPoint object */
    public VO_WarpingPoint()
    {
        this.m_CVPosition								= new Point2f(0.0f, 0.0f);
        this.m_iTriangleIndex							= 0;
        this.m_iPointIndex								= 0;
        this.m_VOTriangle2DStructure					= null;
        this.m_Gradients								= null;
        this.m_Jacobian_One								= null;
        this.m_JacobianMatrix4ShapeModel				= null;
        this.m_JacobianMatrix4GlobalShapeNorm			= null;
        this.m_SteepestDescentImages4ShapeModel			= null;
        this.m_SteepestDescentImages4GlobalShapeNorm	= null;
    }

    /** Copy constructor */
    public VO_WarpingPoint( final VO_WarpingPoint t )
    {
        this.m_CVPosition                              	= t.GetPosition();
        this.m_iTriangleIndex                          	= t.GetTriangleIndex();
        this.m_iPointIndex                             	= t.GetPointIndex();
        this.m_VOTriangle2DStructure                   	= t.GetAAMTriangle2d();
        this.CalcJacobianOne ();
        this.m_JacobianMatrix4ShapeModel            	= t.GetJacobianMatrix();
        this.m_JacobianMatrix4GlobalShapeNorm          	= t.GetJacobianMatrix4GlobalShapeNorm();
        this.m_Gradients                               	= t.GetGradients();
        this.m_SteepestDescentImages4ShapeModel     	= t.GetSteepestDescentImages4ShapeModel();
        this.m_SteepestDescentImages4GlobalShapeNorm   	= t.GetSteepestDescentImages4GlobalShapeNorm();
    }

    /** Calculate Jacobian first part for later Jacobian matrix calculation */
    public void						CalcJacobianOne()
    {
        float x, y, x1, x2, x3, y1, y2, y3;
        this.m_Jacobian_One = new float [3];

        x = this.m_CVPosition.x;
        y = this.m_CVPosition.y;

        x1 = this.m_VOTriangle2DStructure.GetA2DPoint(0).x;
        x2 = this.m_VOTriangle2DStructure.GetA2DPoint(1).x;
        x3 = this.m_VOTriangle2DStructure.GetA2DPoint(2).x;
        y1 = this.m_VOTriangle2DStructure.GetA2DPoint(0).y;
        y2 = this.m_VOTriangle2DStructure.GetA2DPoint(1).y;
        y3 = this.m_VOTriangle2DStructure.GetA2DPoint(2).y;

        this.m_Jacobian_One[0] = y*x3-y3*x+x*y2-x2*y+x2*y3-x3*y2;
        this.m_Jacobian_One[1] = -y*x3+x1*y+x3*y1+y3*x-x1*y3-x*y1;
        this.m_Jacobian_One[2] = -x*y2+x*y1+x1*y2+x2*y-x2*y1-x1*y;

        // make sure m_Jacobian_One is bigger than 0
        for (int i = 0; i < this.m_Jacobian_One.length; i++)
        {
            this.m_Jacobian_One[i] /= this.m_VOTriangle2DStructure.GetdD();
            if (this.m_Jacobian_One[i] < Float.MIN_VALUE)
                this.m_Jacobian_One[i] = 0.0f;
        }
    }

    /** Calculate Jacobian matrix for later Steepest Descent Image Calculation */
    public void						CalcJacobianMatrix4ShapeModel(final float[][] TruncatedAlignedShapesEigenVectors)
    {
        int NbOfShapesParameters = TruncatedAlignedShapesEigenVectors.length;
    	int NbOfShapePoints = TruncatedAlignedShapesEigenVectors[0].length/2;
        // for a certain temppixeltriangle.m_CVPosition, the Jacobian[i] are different

        this.m_JacobianMatrix4ShapeModel = new float[2][NbOfShapesParameters];

        for ( int np = 0; np < NbOfShapesParameters; np++)
        {
            this.m_JacobianMatrix4ShapeModel[0][np] =
    				this.m_Jacobian_One[0] * TruncatedAlignedShapesEigenVectors[np][this.m_VOTriangle2DStructure.GetVertexIndex(0)]
    			  + this.m_Jacobian_One[1] * TruncatedAlignedShapesEigenVectors[np][this.m_VOTriangle2DStructure.GetVertexIndex(1)]
    			  + this.m_Jacobian_One[2] * TruncatedAlignedShapesEigenVectors[np][this.m_VOTriangle2DStructure.GetVertexIndex(2)];

            this.m_JacobianMatrix4ShapeModel[1][np] =
    				this.m_Jacobian_One[0] * TruncatedAlignedShapesEigenVectors[np][this.m_VOTriangle2DStructure.GetVertexIndex(0)+NbOfShapePoints]
    			  + this.m_Jacobian_One[1] * TruncatedAlignedShapesEigenVectors[np][this.m_VOTriangle2DStructure.GetVertexIndex(1)+NbOfShapePoints]
    			  + this.m_Jacobian_One[2] * TruncatedAlignedShapesEigenVectors[np][this.m_VOTriangle2DStructure.GetVertexIndex(2)+NbOfShapePoints];
        }
    }

    /** Calculate Jacobian matrix for global shape normalization for later Steepest Descent Image Calculation */
    public void						CalcJacobianMatrix4GlobalShapeNorm(final float[][] SimilarityTransformMatrix)
    {
        int NbOfSimilarityTransformParameters = SimilarityTransformMatrix.length;
    	int NbOfShapePoints = SimilarityTransformMatrix[0].length/2;
        // for a certain temppixeltriangle.m_CVPosition, the Jacobian[i] are different

        this.m_JacobianMatrix4GlobalShapeNorm = new float[2][NbOfSimilarityTransformParameters];

        for (int np = 0; np < NbOfSimilarityTransformParameters; np++)
        {
            this.m_JacobianMatrix4GlobalShapeNorm[0][np] = 
    			this.m_Jacobian_One[0] * SimilarityTransformMatrix[np][this.m_VOTriangle2DStructure.GetVertexIndex(0)]
    		  + this.m_Jacobian_One[1] * SimilarityTransformMatrix[np][this.m_VOTriangle2DStructure.GetVertexIndex(1)]
    		  + this.m_Jacobian_One[2] * SimilarityTransformMatrix[np][this.m_VOTriangle2DStructure.GetVertexIndex(2)];

            this.m_JacobianMatrix4GlobalShapeNorm[1][np] = 
    			this.m_Jacobian_One[0] * SimilarityTransformMatrix[np][this.m_VOTriangle2DStructure.GetVertexIndex(0)+NbOfShapePoints]
    		  + this.m_Jacobian_One[1] * SimilarityTransformMatrix[np][this.m_VOTriangle2DStructure.GetVertexIndex(1)+NbOfShapePoints]
    		  + this.m_Jacobian_One[2] * SimilarityTransformMatrix[np][this.m_VOTriangle2DStructure.GetVertexIndex(2)+NbOfShapePoints];
        }
    }

    /** Calculate Steepest Descent Image */
    public void                     CalcSteepestDescentImages4ShapeModel(int inChannels)
    {
        this.m_SteepestDescentImages4ShapeModel = new float[inChannels][this.m_JacobianMatrix4ShapeModel[0].length];

        for (int i = 0; i < this.m_SteepestDescentImages4ShapeModel[0].length; i++)
        {
            for (int k = 0; k < inChannels; k++)
            {
                this.m_SteepestDescentImages4ShapeModel[k][i] =
                    this.m_Gradients[k][0] * this.m_JacobianMatrix4ShapeModel[0][i] +
                    this.m_Gradients[k][1] * this.m_JacobianMatrix4ShapeModel[1][i];
            }
        }
    }

    /** Calculate Steepest Descent Image Calculation for global shape normalization */
    public void                     CalcSteepestDescentImages4GlobalShapeNorm(int inChannels)
    {
        this.m_SteepestDescentImages4GlobalShapeNorm = new float [inChannels][this.m_JacobianMatrix4GlobalShapeNorm[0].length];

        for (int i = 0; i < this.m_SteepestDescentImages4GlobalShapeNorm[0].length; i++)
        {
            for (int k = 0; k < inChannels; k++)
            {
                this.m_SteepestDescentImages4GlobalShapeNorm[k][i] =
                    this.m_Gradients[k][0] * this.m_JacobianMatrix4GlobalShapeNorm[0][i] +
                    this.m_Gradients[k][1] * this.m_JacobianMatrix4GlobalShapeNorm[1][i];
            }
        }
    }

    /** Get this warping point coordinates */
    public Point2f    		    	GetPosition() {return this.m_CVPosition;}

    /** Get this warping point triangle index in AAM model triangle vector */
    public int    		            GetTriangleIndex() {return this.m_iTriangleIndex;}

    /** Get this warping point index in AAM model point vector */
    public int    		            GetPointIndex() {return this.m_iPointIndex;}

    /** Get the triangle that this warping point belong to */
    public VO_Triangle2DStructure   GetAAMTriangle2d() {return this.m_VOTriangle2DStructure;}

    /** Get Jacobian one 1*3 vector - alpha, beta, gamma */
    public float[]                  GetJacobianOne() { return this.m_Jacobian_One;}

    /** Get Jacobian matrix of this warping point */
    public float[][]          		GetJacobianMatrix() { return this.m_JacobianMatrix4ShapeModel;}

    /** Get Jacobian matrix for global shape normalization of this warping point */
    public float[][]          		GetJacobianMatrix4GlobalShapeNorm() { return this.m_JacobianMatrix4GlobalShapeNorm;}

    /** Get gradients for this warping point */
    public float[][]	    		GetGradients() {return this.m_Gradients;}

    /** Get steepest descent image for this warping point */
    public float[][]	    		GetSteepestDescentImages4ShapeModel() {return this.m_SteepestDescentImages4ShapeModel;}

    /** Get steepest descent image for global shape normalization for this warping point */
    public float[][]	    		GetSteepestDescentImages4GlobalShapeNorm() { return this.m_SteepestDescentImages4GlobalShapeNorm;}


    /** Set this warping point coordinates */
    public void    		            SetPosition(Point2f iPoint) {this.m_CVPosition = iPoint;}
    public void    		            SetPosition(float x, float y) {this.m_CVPosition = new Point2f(x,y);}

    /** Set this warping point triangle index in AAM model triangle vector */
    public void    		            SetTriangleIndex(int iTriangleIndex) {this.m_iTriangleIndex = iTriangleIndex;}

    /** Set this warping point index in AAM model point vector */
    public void    		            SetPointIndex(int iPointIndex) {this.m_iPointIndex = iPointIndex;}

    /** Set 3 vertexes of the triangle that this point belong to, with input in format of a vector of points */
    public void                     SetTriangle2DStructure(final Point2f[] inVertexes, final int[] iIndexes)
    {
									assert(inVertexes.length == 3);
									this.m_VOTriangle2DStructure = new VO_Triangle2DStructure(inVertexes, iIndexes);
    }

    /** Set the triangle that this point belong to */
    public void                     SetTriangle2DStructure(final VO_Triangle2DStructure inAAMTriangle2d)
    {
                                    this.m_VOTriangle2DStructure = inAAMTriangle2d;
    }

    /** Set Jacobian one 1*3 vector - alpha, beta, gamma */
    public void                     SetJacobianOne(final float[] iJO)
    {
    	this.m_Jacobian_One = iJO;
	}

    /** Set Jacobian matrix for this warping point */
    public void	                    SetJacobianMatrix(final float[][] iJM)
    {
    	this.m_JacobianMatrix4ShapeModel = iJM;
	}

    /** Set Jacobian matrix for global shape normalization for this warping point */
    public void	                    SetJacobianMatrix4GlobalShapeNorm(final float[][] iJM4GlobalShapeNorm)
    {
    	this.m_JacobianMatrix4GlobalShapeNorm = iJM4GlobalShapeNorm;
	}

    /** Set gradients for this warping point */
    public void                     SetGradients(final float[][] iGradients)
    {
    	this.m_Gradients = iGradients;
	}

    /** Set steepest descent image for this warping point */
    public void	                    SetSteepestDescentImages(final float[][] iSDI)
    {
    	this.m_SteepestDescentImages4ShapeModel = iSDI;
	}

    /** Set steepest descent image for global shape normalization for this warping point */
    public void	                    SetSteepestDescentImages4GlobalShapeNorm(final float[][] iSDI4GlobalShapeNorm)
    {
    	this.m_SteepestDescentImages4GlobalShapeNorm = iSDI4GlobalShapeNorm;
	}

}
