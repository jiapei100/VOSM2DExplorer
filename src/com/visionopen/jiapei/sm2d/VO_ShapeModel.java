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
* Version:          1.0                                                                             *
* Author:           JIA Pei                                                                         *
* Contact:          jp4work@gmail.com                                                               *
* URL:              http://www.visionopen.com                                                       *
* Create Date:      2010-12-25                                                                      *
* Modify Date:      2014-05-07                                                                      *
* Modify Date:      2021-05-07                                                                      *
****************************************************************************************************/

package com.visionopen.jiapei.sm2d;

import java.io.*;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import org.eclipse.swt.graphics.ImageData;
import javax.vecmath.Point2f;

import com.visionopen.jiapei.smbasic.VO_Edge;
import com.visionopen.jiapei.smbasic.VO_FaceParts;
import com.visionopen.jiapei.smbasic.VO_Shape;
import com.visionopen.jiapei.smbasic.VO_Shape2DInfo;
import com.visionopen.jiapei.smbasic.VO_Triangle2DStructure;
import com.visionopen.jiapei.utils.PCA;




public class VO_ShapeModel
{
	/** PCA transform for shape, including eigenvectors, eigenvalues, and mean */
	public PCA							m_PCAAlignedShape;

    /** Number of training samples. For IMM: 240 */
    protected int						m_iNbOfSamples;

	/** 2D or 3D, 116/58 = 2 */
    protected int						m_iNbOfShapeDim;

    /** Number of points to describe per shape. For IMM: 58 */
    protected int						m_iNbOfPoints;

    /** length of shape vector in format of xxx...yyy.... For IMM: 58*2=116 */
    protected int						m_iNbOfShapes;

    /** Shape eigens at most before PCA. For IMM: min (116, 240) = 116 */
    protected int						m_iNbOfEigenShapesAtMost;

    /** Number of shape model eigens. For IMM: 21 */
    protected int						m_iNbOfShapeEigens;

    /** Number of edges */
    protected int						m_iNbOfEdges;

	/** Number of triangles */
    protected int						m_iNbOfTriangles;
	
    /** Reference shape which is of size close to "1", but not guaranteed */
    protected VO_Shape         			m_VOAlignedMeanShape;

    /** Reference shape which is scaled back to the original size and translated to (left top most = origin) */
    protected VO_Shape         			m_VOReferenceShape;

    /** The reference shape average size : 582.425 */
    protected float						m_fAverageShapeSize;

    /** Truncate Percentage for shape model PCA. Normally, 0.95 */
    protected float               		m_fTruncatedPercent_Shape;

    /** All loaded shapes in a vector format. For IMM, 240*(58*2) */
    protected VO_Shape[]         		m_vShapes;

    /** All aligned shapes in a vector format. For IMM, 240*(58*2) */
    protected VO_Shape[]         		m_vAlignedShapes;

    /** shape information */
    protected VO_Shape2DInfo[]   		m_vShape2DInfo;

    /** face parts information, seems to have the duplicate information as m_vShape2DInfo, but not! */
    protected VO_FaceParts           	m_FaceParts;

    /** Edges in the template shape, only contain the index pairs. For IMM,  152 */
    protected VO_Edge[]					m_vEdge;

    /** Unnormalized triangles in the template shape. For IMM, 95 */
    protected VO_Triangle2DStructure[]	m_vTemplateTriangle2D;

    /** Normalized triangles in the template shape - just replace the vertexes in m_vTemplateTriangle2D by corresponding vertexes of m_VOAlignedMeanShape. For IMM, 95 */
    protected VO_Triangle2DStructure[]	m_vNormalizedTriangle2D;
    
	/** Normalized Point distribution model */
	VO_Point2DDistributionModel			m_VOPDM;

    /** Initialization */
    protected void init() {
	    this.m_iNbOfSamples					= 0;
		this.m_iNbOfShapeDim				= 0;
	    this.m_iNbOfPoints					= 0;
	    this.m_iNbOfShapes					= 0;
	    this.m_iNbOfShapeEigens				= 0;
	    this.m_iNbOfEigenShapesAtMost		= 0;
	    this.m_iNbOfEdges					= 0;
		this.m_iNbOfTriangles				= 0;
	    this.m_fAverageShapeSize			= 0.0f;
	    this.m_fTruncatedPercent_Shape  	= 0.95f;
		
	    this.m_PCAAlignedShape				= null;
		this.m_VOAlignedMeanShape			= null;
		this.m_VOReferenceShape				= null;
		this.m_vShapes						= null;
		this.m_vAlignedShapes				= null;
		this.m_vShape2DInfo					= null;
		this.m_FaceParts					= null;
		this.m_vEdge						= null;
		this.m_vTemplateTriangle2D			= null;
		this.m_vNormalizedTriangle2D		= null;
	}
	
	public VO_ShapeModel() {
		init();
	}
	
    /** Align all shapes */
    public static float					VO_AlignAllShapes(final VO_Shape[] vShapes, VO_Shape[] alignedShapes)
    {
    	return 0.0f;
    }

    /** Rescale all aligned shapes */
    public static void 					VO_RescaleAllAlignedShapes(final VO_Shape meanAlignedShape, VO_Shape[] alignedShapes)
    {
        int NbOfSamples = alignedShapes.length;

        // Explained by JIA Pei. 2010-02-07. scale back so that the mean shape size is equal to 1.0
        for(int i = 0; i < NbOfSamples; i++)
        {
            VO_ShapeModel.VO_RescaleOneAlignedShape(meanAlignedShape, alignedShapes[i]);
        }
    }

    /** Rescale one aligned shape */
    public static void 					VO_RescaleOneAlignedShape(final VO_Shape meanAlignedShape, VO_Shape alignedShape)
    {
        float ts = (float) Math.sqrt ( Math.abs(alignedShape.dot(meanAlignedShape) ) );
        alignedShape.Scale( 1.0f/ts );
    }

    /** Returns the mean shape of all shapes */
    public static void					VO_CalcMeanShape(final VO_Shape[] vShapes, VO_Shape meanShape)
    {
        int NbOfSamples    = vShapes.length;
        meanShape = vShapes[0];

        for(int i = 1; i < NbOfSamples; i++)
        {
            meanShape = meanShape.add(vShapes[i]);
        }

        meanShape.Scale(1.0f/(float)NbOfSamples);
    }

    /** Judge whether the point "pt" is in convex hull "ch" */
    public static boolean               VO_IsPointInConvexHull(final Point2f pt, final float[][] ch, boolean includingBoundary)
    {
    	return false;
    }

    /** Judge whether edge indexed by (ind1+ind2) is already in the vector of "edges" */
    public static boolean               VO_EdgeHasBeenCounted(final VO_Edge[] edges, int ind1, int ind2)
    {
    	return false;
    }

    /** Judge whether triangle t is already in the vector of "triangles" */
    public static boolean               VO_TriangleHasBeenCounted(final VO_Triangle2DStructure[] triangles, final int[] t)
    {
    	return false;
    }

//    /** Build Edges */
//    public static int            		VO_BuildEdges(final VO_Shape iShape, final CvSubdiv2D* Subdiv, VO_Edge[] outEdges)
//    {
//    	
//    }

    /** Build triangles */
    public static int            		VO_BuildTriangles(final VO_Shape iShape, final VO_Edge[] edges, VO_Triangle2DStructure[] outTriangles)
    {
    	return 0;
    }

    /** Build Delaunay triangulation mesh */
    public static void                 	VO_BuildTemplateMesh(final VO_Shape iShape, VO_Edge[] edges, VO_Triangle2DStructure[] triangles)
    {
    	
    }

    /** Build triangulation mesh according to shapeInfo */
    public static void                 	VO_BuildTemplateMesh(final VO_Shape iShape, final VO_FaceParts iFaceParts, VO_Edge[] edges, VO_Triangle2DStructure[] triangles)
    {
    	
    }

    /** Judge whether the shape is inside an image */
    public static boolean               VO_IsShapeInsideImage(final VO_Shape iShape, final ImageData img)
    {
    	Rectangle2D.Float rect = iShape.GetShapeRect();

        // The following line should be avoided. explained by JIA Pei. Try the second line!
        //if ( (rect.x > 0) && (rect.y > 0) && ( (rect.x + rect.width) <= img.cols ) && ( (rect.y + rect.height) <= img.rows ) )
//        if ( (rect.x >= 0.0) && (rect.y >= 0.0) && ( (rect.x + rect.width) < (float)img.width ) && ( (rect.y + rect.height) < (float)img.height ) )
    	if ( (rect.x >= 0.0) && (rect.y >= 0.0) && ( (rect.x + rect.width) < (float)img.width - 1.0f - Float.MIN_VALUE ) && ( (rect.y + rect.height) < (float)img.height - 1.0f - Float.MIN_VALUE ) )
            return true;
        else
            return false;
    }

    /** Reference shape inShape back to aligned outShape */
    public static void                 	VO_ReferenceShapeBack2Aligned(final VO_Shape inShape, float shapeSD, VO_Shape outShape)
    {
    	outShape 						= new VO_Shape(inShape);
    	float[] center 					= com.visionopen.jiapei.utils.Common.Neg( outShape.CenterOfGravity() );		// centralize first
    	outShape.Translate( center );
        outShape.Scale(1.0f/shapeSD );	// scale second
    }

	/** Calculate the bounding rectangle from a list of rectangles */
    public static Rectangle				VO_CalcBoundingRectFromTriangles(final VO_Triangle2DStructure[] triangles)
    {
    	VO_Shape shape = VO_Triangle2DStructure.Triangle2D2Shape(triangles);

    	return shape.GetShapeBoundRect();
    }

	/** Shape parameters constraints */
    public void							VO_ShapeParameterConstraint(float[] ioP, float nSigma)
    {
    	int NbOfShapeParameterConstraints = ioP.length;
    	float ct = 0.0f;

        for (int i = 0; i < NbOfShapeParameterConstraints; ++i)
        {
        	ct = nSigma * (float) Math.sqrt(this.m_PCAAlignedShape.eigenvalues[i]);
            if ( ioP[i] > ct )
            {
                ioP[i] = ct;
            }
            else if ( ioP[i] < -ct )
            {
                ioP[i] = -ct;
            }
        }
    }
    
    /** Shape projected to shape parameters*/
    public void   	                	VO_AlignedShapeProjectToSParam(final VO_Shape iShape, float[] outP)
    {
    	this.m_PCAAlignedShape.project(iShape.GetTheShapeInARow(), outP);
    }

    /** Shape parameters back projected to shape parameters*/
    public void   	                	VO_SParamBackProjectToAlignedShape(final float[] inP, VO_Shape oShape, int dim)
    {
    	oShape.SetTheShape(this.m_PCAAlignedShape.backProject(inP), dim);
    }

    /** shape -> Procrustes analysis -> project to shape parameters */
    public void   	                	VO_CalcAllParams4AnyShapeWithConstrain(final float[][] iShape, float[][] oShape, float[] outP, float norm, float[] angles, float[] t )
    {
    	
    }

    /** shape -> Procrustes analysis -> project to shape parameters */
    public void                        	VO_CalcAllParams4AnyShapeWithConstrain(VO_Shape ioShape, float[] outP, float norm, float[] angles, float[] t )
    {
    	
    }

    /** Build Shape Model */
    public void							VO_BuildShapeModel(final String[] allLandmarkFiles4Training,
															final String shapeinfoFileName, 
															int database,
															float TPShape, 
															boolean useKnownTriangles)
    {
    	
    }

    /** Save Shape Model, to a specified folder */
    public void                        	VO_Save(final String fd)
    {
    	
    }

    /** Load all parameters */
	public void 						VO_LoadOthers(String fd)
	{
		if(fd != "")
		{
		    String folderDir = fd+"/ShapeModel";
		    String filename = new String();
		    FileReader filereader = null;
		    BufferedReader in = null;
		    String str = new String();
		    String ss[] = null;

			// m_vShapes
			try {
				filename = folderDir + "/m_vShapes.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_vShapes = new VO_Shape[this.m_iNbOfSamples];
		        
		        for(int i = 0; i < this.m_iNbOfSamples; i++)
		        {
		        	this.m_vShapes[i] = new VO_Shape(this.m_iNbOfShapeDim, this.m_iNbOfPoints);
		        	for(int j = 0; j < this.m_iNbOfShapeDim; j++)
		        	{
				        while ( (str = in.readLine())!=null )
				        {
				        	if(str.trim().length() != 0)
				        	{
						        ss = str.split(" ");
						        assert(this.m_iNbOfPoints == ss.length);
						        for(int k = 0; k < this.m_iNbOfPoints; k++)
						        {
						        	this.m_vShapes[i].SetAShape(j, k, Float.parseFloat(ss[k]));
						        }
						        break;
				        	}
				        }
		        	}
		        }
		        
		        in.close();
		        filereader.close();
		        	
			} catch (IOException e){
				e.printStackTrace();
			}

		    // m_vAlignedShapes
			try {
				filename = folderDir + "/m_vAlignedShapes.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_vAlignedShapes = new VO_Shape[this.m_iNbOfSamples];
		        
		        for(int i = 0; i < this.m_iNbOfSamples; i++)
		        {
		        	this.m_vAlignedShapes[i] = new VO_Shape(this.m_iNbOfShapeDim, this.m_iNbOfPoints);
		        	for(int j = 0; j < this.m_iNbOfShapeDim; j++)
		        	{
				        while ( (str = in.readLine())!=null )
				        {
				        	if(str.trim().length() != 0)
				        	{
						        ss = str.split(" ");
						        assert(this.m_iNbOfPoints == ss.length);
						        for(int k = 0; k < this.m_iNbOfPoints; k++)
						        {
						        	this.m_vAlignedShapes[i].SetAShape(j, k, Float.parseFloat(ss[k]));
						        }
						        break;
				        	}
				        }
		        	}
		        }
		        
		        in.close();
		        filereader.close();
		        	
			} catch (IOException e){
				e.printStackTrace();
			}
		}
	}
	
    /** Load parameters for fitting */
    public void                        	VO_LoadParameters4Fitting(final String fd)
    {
		if(fd != "")
		{
			init();
		    String folderDir = fd+"/ShapeModel";
		    String filename = new String();
		    FileReader filereader = null;
		    BufferedReader in = null;
		    String str = new String();
		    String ss[] = null;

			// Read ShapeModel.txt
			try {
				filename = folderDir + "/ShapeModel.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();
		        str = in.readLine();
		        this.m_iNbOfSamples = Integer.parseInt(str);
	            
		        str = in.readLine();
		        str = in.readLine();
		        this.m_iNbOfShapeDim = Integer.parseInt(str);
		        
		        str = in.readLine();
		        str = in.readLine();
		        this.m_iNbOfPoints = Integer.parseInt(str);
		        
		        str = in.readLine();
		        str = in.readLine();
		        this.m_iNbOfShapes = Integer.parseInt(str);
		        
		        str = in.readLine();
		        str = in.readLine();
		        this.m_iNbOfEigenShapesAtMost = Integer.parseInt(str);
		        
		        str = in.readLine();
		        str = in.readLine();
		        this.m_iNbOfShapeEigens = Integer.parseInt(str);
		        
		        str = in.readLine();
		        str = in.readLine();
		        this.m_iNbOfEdges = Integer.parseInt(str);
		        
		        str = in.readLine();
		        str = in.readLine();
		        this.m_iNbOfTriangles = Integer.parseInt(str);
		        
		        str = in.readLine();
		        str = in.readLine();
		        this.m_fAverageShapeSize = Float.parseFloat(str);
		        
		        str = in.readLine();
		        str = in.readLine();
		        this.m_fTruncatedPercent_Shape = Float.parseFloat(str);
		        
		        in.close();
		        filereader.close();
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }

			this.m_PCAAlignedShape = new PCA();

		    // m_PCAAlignedShapeMean
			try {
				filename = folderDir + "/m_PCAAlignedShapeMean.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_PCAAlignedShape.mean = new float[this.m_iNbOfShapes];
		        
		        if ( (str = in.readLine())!=null)
		        {
		        	ss = str.split(" ");
		        	assert (this.m_iNbOfShapes == ss.length);
		        	for(int i = 0; i < this.m_iNbOfShapes; i++)
		        	{
		        		this.m_PCAAlignedShape.mean[i] = Float.parseFloat(ss[i]);
		        	}
		        }
		        
		        in.close();
		        filereader.close();
		        
			} catch (IOException e){
				e.printStackTrace();
			}

			// m_PCAAlignedShapeEigenValues
			try {
				filename = folderDir + "/m_PCAAlignedShapeEigenValues.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_PCAAlignedShape.eigenvalues = new float[this.m_iNbOfShapeEigens];
		        
	        	for(int i = 0; i < this.m_iNbOfShapeEigens; i++)
	        	{
	        		if ( (str = in.readLine())!=null)
	        			this.m_PCAAlignedShape.eigenvalues[i] = Float.parseFloat(str);
	        	}
		        
		        in.close();
		        filereader.close();
		        
			} catch (IOException e){
				e.printStackTrace();
			}
			
			// m_PCAAlignedShapeEigenVectors
			try {
				filename = folderDir + "/m_PCAAlignedShapeEigenVectors.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_PCAAlignedShape.eigenvectors = new float[this.m_iNbOfShapeEigens][this.m_iNbOfShapes];

		        for(int i = 0; i < this.m_iNbOfShapeEigens; i++)
		        {
		        	if ( (str = in.readLine())!=null)
			        	ss = str.split(" ");
	        		assert (this.m_iNbOfShapes == ss.length);
		        	for(int j = 0; j < this.m_iNbOfShapes; j++)
		        	{
		        		this.m_PCAAlignedShape.eigenvectors[i][j] = Float.parseFloat(ss[j]);
		        	}
		        }
		        
		        in.close();
		        filereader.close();
		        
			} catch (IOException e){
				e.printStackTrace();
			}
			
		    // m_VOAlignedMeanShape
			try {
				filename = folderDir + "/m_VOAlignedMeanShape.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_VOAlignedMeanShape = new VO_Shape(this.m_iNbOfShapeDim, this.m_iNbOfPoints);
		        for(int i = 0; i < this.m_iNbOfShapeDim; i++)
		        {
		        	if ( (str = in.readLine())!=null )
			        {
				        ss = str.split(" ");
				        for(int j = 0; j < this.m_iNbOfPoints; j++)
				        {
				        	this.m_VOAlignedMeanShape.SetAShape(i, j, Float.parseFloat(ss[j]));
				        }
			        }
		        }
		        
		        in.close();
		        filereader.close();
		        	
			} catch (IOException e){
				e.printStackTrace();
			}
			
		    // m_VOReferenceShape
			try {
				filename = folderDir + "/m_VOReferenceShape.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_VOReferenceShape = new VO_Shape(this.m_iNbOfShapeDim, this.m_iNbOfPoints);
		        for(int i = 0; i < this.m_iNbOfShapeDim; i++)
		        {
		        	if ( (str = in.readLine())!=null )
			        {
				        ss = str.split(" ");
				        for(int j = 0; j < this.m_iNbOfPoints; j++)
				        {
				        	this.m_VOReferenceShape.SetAShape(i, j, Float.parseFloat(ss[j]));
				        }
			        }
		        }
		        
		        in.close();
		        filereader.close();
		        	
			} catch (IOException e){
				e.printStackTrace();
			}
			
		    // m_vShape2DInfo & m_FaceParts
			filename = folderDir + "/m_vShape2DInfo.txt";
			com.visionopen.jiapei.smbasic.VO_Shape2DInfo.ReadShape2DInfo(filename, this.m_vShape2DInfo, this.m_FaceParts );

		    // m_vEdge
			try {
				filename = folderDir + "/m_vEdge.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_vEdge = new VO_Edge[this.m_iNbOfEdges];

		        for(int i = 0; i < this.m_iNbOfEdges; i++)
		        {
		        	this.m_vEdge[i] = new VO_Edge();
		        	if ( (str = in.readLine())!=null)
			        	ss = str.split(" ");
	        		this.m_vEdge[i].SetIndex1(Integer.parseInt(ss[0]));
	        		this.m_vEdge[i].SetIndex2(Integer.parseInt(ss[1]));
		        }

		        in.close();
		        filereader.close();
		        
			} catch (IOException e){
				e.printStackTrace();
			}

		     // m_vTemplateTriangle2D
			try {
				filename = folderDir + "/m_vTemplateTriangle2D.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_vTemplateTriangle2D = new VO_Triangle2DStructure[this.m_iNbOfTriangles];

		        for(int i = 0; i < this.m_iNbOfTriangles; i++)
		        {
			        int[] vertIdx = new int[3];
			        float[][] triangleCoords = new float[2][3];
		        	this.m_vTemplateTriangle2D[i] = new VO_Triangle2DStructure();
		        	if ( (str = in.readLine())!=null)
		        	{
			        	ss = str.split(" ");
			        	vertIdx[0] = Integer.parseInt(ss[0]);
			        	vertIdx[1] = Integer.parseInt(ss[3]);
			        	vertIdx[2] = Integer.parseInt(ss[6]);
			        	triangleCoords[0][0] = Float.parseFloat(ss[1]);
			        	triangleCoords[1][0] = Float.parseFloat(ss[2]);
			        	triangleCoords[0][1] = Float.parseFloat(ss[4]);
			        	triangleCoords[1][1] = Float.parseFloat(ss[5]);
			        	triangleCoords[0][2] = Float.parseFloat(ss[7]);
			        	triangleCoords[1][2] = Float.parseFloat(ss[8]);
			        	this.m_vTemplateTriangle2D[i].SetTheShape(triangleCoords);
		        		this.m_vTemplateTriangle2D[i].SetVertexIndexes(vertIdx);
		        		this.m_vTemplateTriangle2D[i].SetdD(Float.parseFloat(ss[9]));
		        	}
		        }

		        in.close();
		        filereader.close();

			} catch (IOException e){
				e.printStackTrace();
			}

		    // m_vNormalizedTriangle2D
			try {
				filename = folderDir + "/m_vNormalizedTriangle2D.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_vNormalizedTriangle2D = new VO_Triangle2DStructure[this.m_iNbOfTriangles];
		        int[] vertIdx = new int[3];
		        float[][] triangleCoords = new float[2][3];

		        for(int i = 0; i < this.m_iNbOfTriangles; i++)
		        {
		        	this.m_vNormalizedTriangle2D[i] = new VO_Triangle2DStructure();
		        	if ( (str = in.readLine())!=null)
		        	{
			        	ss = str.split(" ");
			        	vertIdx[0] = Integer.parseInt(ss[0]);
			        	vertIdx[1] = Integer.parseInt(ss[3]);
			        	vertIdx[2] = Integer.parseInt(ss[6]);
			        	triangleCoords[0][0] = Float.parseFloat(ss[1]);
			        	triangleCoords[1][0] = Float.parseFloat(ss[2]);
			        	triangleCoords[0][1] = Float.parseFloat(ss[4]);
			        	triangleCoords[1][1] = Float.parseFloat(ss[5]);
			        	triangleCoords[0][2] = Float.parseFloat(ss[7]);
			        	triangleCoords[1][2] = Float.parseFloat(ss[8]);
			        	this.m_vNormalizedTriangle2D[i].SetTheShape(triangleCoords);
		        		this.m_vNormalizedTriangle2D[i].SetVertexIndexes(vertIdx);
		        		this.m_vNormalizedTriangle2D[i].SetdD(Float.parseFloat(ss[9]));
		        	}
		        }

		        in.close();
		        filereader.close();

			} catch (IOException e){
				e.printStackTrace();
			}
		}
		
//		this.m_VOPDM.VO_LoadParameters4Fitting(fd);
    }

    /** Gets and Sets */
	public float[]                 		GetAlignedShapesMean() {return this.m_PCAAlignedShape.mean;}
	public float[]                 		GetAlignedShapesEigenValues() {return this.m_PCAAlignedShape.eigenvalues;}
	public float[][]                 	GetAlignedShapesEigenVectors() {return this.m_PCAAlignedShape.eigenvectors;}
	public int                			GetNbOfSamples() {return this.m_iNbOfSamples;}
	public int                			GetNbOfShapeDim() {return this.m_iNbOfShapeDim;}
	public int                			GetNbOfPoints() {return this.m_iNbOfPoints;}
	public int                			GetNbOfShapes() {return this.m_iNbOfShapes;}
	public int                			GetNbOfEigenShapesAtMost() {return this.m_iNbOfEigenShapesAtMost;}
	public int                			GetNbOfShapeEigens() {return this.m_iNbOfShapeEigens;}
	public int                			GetNbOfTriangles() {return this.m_iNbOfTriangles;}
	public VO_Shape						GetAlignedMeanShape() { return this.m_VOAlignedMeanShape;}
	public VO_Shape                 	GetReferenceShape() {return this.m_VOReferenceShape;}
	public float                       	GetAverageShapeSize() {return this.m_fAverageShapeSize;}
	public float                       	GetTruncatedPercent_Shape() {return this.m_fTruncatedPercent_Shape;}
	public VO_Shape[]         			GetShapes() {return this.m_vShapes;}
	public VO_Shape[]         			GetAlignedShapes() {return this.m_vAlignedShapes;}
	public VO_Shape2DInfo[]   			GetShapeInfo() {return this.m_vShape2DInfo;}
	public VO_FaceParts					GetFaceParts() {return this.m_FaceParts;}
	public VO_Edge[]          			GetEdge() {return this.m_vEdge;}
	public VO_Triangle2DStructure[]		GetTriangle2D() {return this.m_vTemplateTriangle2D;}
	public VO_Triangle2DStructure[]  	GetNormalizedTriangle2D() {return this.m_vNormalizedTriangle2D;}
	
    public static void main(String[] args) {
    	String fd = "/home/jiapei/MyPrograms/Eclipse/AAMJava3D/traineddata/2D/JIAPei";
    	VO_ShapeModel shapemodel = new VO_ShapeModel();
    	shapemodel.VO_LoadParameters4Fitting(fd);
    	shapemodel.VO_LoadOthers(fd);
	}
}
