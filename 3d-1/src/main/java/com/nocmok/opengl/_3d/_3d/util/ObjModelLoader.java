package com.nocmok.opengl._3d._3d.util;

import com.nocmok.opengl._3d._3d.model.MixedModel;
import com.nocmok.opengl._3d._3d.model.PolygonModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class ObjModelLoader {

    public static PolygonModel loadModel(URL url) {
        try (var in = new BufferedReader(
                new InputStreamReader(url.openStream()))) {

            var points = new ArrayList<double[]>();
            var topology = new ArrayList<int[]>();

            String line;
            while ((line = in.readLine()) != null) {
                if (line.startsWith("v ")) {
                    String[] pointStr = line.substring(2).split("\\s+");
                    double[] point = new double[4];
                    point[3] = 1;
                    for (int i = 0; i < 3; ++i) {
                        point[i] = Double.parseDouble(pointStr[i]);
                    }
                    points.add(point);
                } else if (line.startsWith("f ")) {
                    String[] topologyStr = line.substring(2).split("\\s+");
                    int[] polygon = new int[topologyStr.length];
                    for (int i = 0; i < topologyStr.length; ++i) {
                        polygon[i] = Integer.parseInt(topologyStr[i].split("/")[0]);
                    }
                    topology.add(polygon);
                }
            }

            double[][] pointsArr = new double[points.size()][];
            int[][] topologyArr = new int[topology.size()][];

            for (int i = 0; i < pointsArr.length; ++i) {
                pointsArr[i] = points.get(i);
            }

            int polygonDegree = 0;

            for (int i = 0; i < topologyArr.length; ++i) {
                topologyArr[i] = topology.get(i);
                for(int j = 0; j < topologyArr[i].length; ++j) {
                    if(topologyArr[i][j] > 0) {
                        topologyArr[i][j] -= 1;
                    } else if (topologyArr[i][j] < 0){
                        topologyArr[i][j] = pointsArr.length + topologyArr[i][j];
                    }else {
                        throw new RuntimeException("invalid .obj file format");
                    }
                }
                polygonDegree = Integer.max(polygonDegree, topologyArr[i].length);
            }

            return new MixedModel(pointsArr, topologyArr, polygonDegree);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
