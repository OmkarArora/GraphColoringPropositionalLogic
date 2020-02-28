/*
Author : Omkar Arora.
*/
package graphcoloringsat;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class GraphColoringSAT {
    public static void main(String[] args) throws ScriptException {
        int numOfNodes = 4;
        int[][] adjacencyMatrix = {{0,1,1,1},{1,0,0,0},{1,0,0,1},{1,0,1,0}};
        int numOfColors = 3;

//        int numOfNodes = 5;
//        int[][] adjacencyMatrix = {{0,1,1,1,1},{1,0,1,1,1},{1,1,0,1,0},{1,1,1,0,1},{1,1,0,1,0}};
//        int numOfColors = 3;

//        int numOfNodes = 5;
//        int[][] adjacencyMatrix = {{0,1,1,1,1},{1,0,1,1,0},{1,1,0,0,1},{1,1,0,0,1},{1,0,1,1,0}};
//        int numOfColors = 4;

//        int numOfNodes = 6;
//        int[][] adjacencyMatrix = {{0,1,1,1,1,1},{1,0,1,1,0,0},{1,1,0,0,1,0},{1,1,0,0,1,1},{1,0,1,1,0,1},{1,0,0,1,1,0}};
//        int numOfColors = 4;

        int numOfLiterals = numOfNodes * numOfColors;
        boolean[] literals = new boolean[numOfLiterals];
        
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine engine = scriptEngineManager.getEngineByName("js");
        
        //define truth table
        int truthTableSize = (int)Math.pow(2,numOfLiterals);
        List<boolean[]> truthTable = new ArrayList(truthTableSize);
        String satClause = getSatClause(literals,numOfColors,adjacencyMatrix,numOfNodes);
        System.out.println("Propositional Logic Clause = " + satClause);

        truthTable = getTruthTable(truthTable,literals,0);
        String tempClause = satClause;
        
        System.out.println("Possible solutions - ");
        boolean solutionFound = false;
        for(int i=0;i<truthTable.size();i++){
            boolean[] temp = truthTable.get(i);
            for(int j=0;j<numOfLiterals;j++){
                literals[j] = temp[j];
                String searchPattern = "literals[" + j + "]";
                String replacePattern = temp[j] + "";
                tempClause = tempClause.replace(searchPattern, replacePattern);
            } 
            Object result = engine.eval(tempClause);
            boolean satBoolean = Boolean.TRUE.equals(result);
            if(satBoolean == true){
                solutionFound = true;
                int nodeCounter = 1;
                for(int j=0;j<temp.length;j++){
                    if(temp[j] == true){
                        System.out.print("Node:"+ nodeCounter++ + " Color:" + (j%3 + 1) + "   ");
                    }
                }
                System.out.print("\n\n");
            }
            tempClause = satClause;
        } 
        if(!solutionFound){
            System.out.println("No solutions for found");
        }
        
    }
    
    static List<boolean[]> getTruthTable(List<boolean[]> truthTable, boolean[] literals, int index){
        boolean[] tempTable = new boolean[literals.length];
        if(index==literals.length-1){
            literals[index] = true;
            for(int i=0;i<literals.length;i++){
                tempTable[i] = literals[i];
            }
            truthTable.add(tempTable);
            tempTable = new boolean[literals.length];
            literals[index] = false;
            for(int i=0;i<literals.length;i++){
                tempTable[i] = literals[i];
            }
            truthTable.add(tempTable);
            return truthTable;
        }     
        else{
            literals[index] = true;
            truthTable =  getTruthTable(truthTable,literals,index+1);
            literals[index] = false;
            truthTable =  getTruthTable(truthTable,literals,index+1);
            return truthTable;
        }
    }

    static String getSatClause(boolean[] literals, int numOfColors, int[][] adjacencyMatrix, int numOfNodes){
        String satClause = "";
        for(int i=0;i<literals.length;i++){
            int tempI = i;
            satClause += "(";
            for(int j=0;j<numOfColors-1;j++){
                satClause += "literals[" + tempI + "]||";
                tempI++;
            }
            satClause += "literals[" + tempI + "]" ;
            satClause = satClause + ")&&";
            
            String temp = "!(";
            for(int j=i;j<numOfColors+i;j++){
                for(int k=j+1;k<numOfColors+i;k++){
                    temp = "!(";
                    temp += "literals[" + j + "]&&literals[" + k + "]" ;
                    temp = temp + ")&&";
                    satClause += temp;
                }
            }
            i = tempI;
        }
        for(int i =0;i<numOfNodes;i++){
            for(int j=0;j<numOfNodes;j++){
                if(adjacencyMatrix[i][j] == 1){
                    for(int k=0;k<numOfColors;k++){
                        satClause += "!(";
                        satClause += "literals[" + (i * numOfColors + k) + "]&&literals[" + (j * numOfColors + k) + "]";
                        satClause += ")&&";
                    }
                }
            }
        }
        satClause += "true";
        return satClause;
    }
}

//SAMPLE OUTPUTS :-
/*
Input: int numOfNodes = 4;
        int[][] adjacencyMatrix = {{0,1,1,1},{1,0,0,0},{1,0,0,1},{1,0,1,0}};
        int numOfColors = 3;
run:
Possible solutions - 
Node:1 Color:1   Node:2 Color:2   Node:3 Color:2   Node:4 Color:3   

Node:1 Color:1   Node:2 Color:2   Node:3 Color:3   Node:4 Color:2   

Node:1 Color:1   Node:2 Color:3   Node:3 Color:2   Node:4 Color:3   

Node:1 Color:1   Node:2 Color:3   Node:3 Color:3   Node:4 Color:2   

Node:1 Color:2   Node:2 Color:1   Node:3 Color:1   Node:4 Color:3   

Node:1 Color:2   Node:2 Color:1   Node:3 Color:3   Node:4 Color:1   

Node:1 Color:2   Node:2 Color:3   Node:3 Color:1   Node:4 Color:3   

Node:1 Color:2   Node:2 Color:3   Node:3 Color:3   Node:4 Color:1   

Node:1 Color:3   Node:2 Color:1   Node:3 Color:1   Node:4 Color:2   

Node:1 Color:3   Node:2 Color:1   Node:3 Color:2   Node:4 Color:1   

Node:1 Color:3   Node:2 Color:2   Node:3 Color:1   Node:4 Color:2   

Node:1 Color:3   Node:2 Color:2   Node:3 Color:2   Node:4 Color:1   

BUILD SUCCESSFUL (total time: 4 seconds)
*/

/*
Input:  int numOfNodes = 5;
        int[][] adjacencyMatrix = {{0,1,1,1,1},{1,0,1,1,1},{1,1,0,1,0},{1,1,1,0,1},{1,1,0,1,0}};
        int numOfColors = 3;
run:
Possible solutions - 
No solutions for found
BUILD SUCCESSFUL (total time: 32 seconds)
*/
/*
Input: int numOfNodes = 5;
       int[][] adjacencyMatrix = {{0,1,1,1,1},{1,0,1,1,0},{1,1,0,0,1},{1,1,0,0,1},{1,0,1,1,0}};
       int numOfColors = 4;
run:
Possible solutions - 
Node:1 Color:1   Node:2 Color:3   Node:3 Color:2   Node:4 Color:3   Node:5 Color:3   

Node:1 Color:1   Node:2 Color:3   Node:3 Color:2   Node:4 Color:3   Node:5 Color:2   

Node:1 Color:1   Node:2 Color:3   Node:3 Color:2   Node:4 Color:1   Node:5 Color:3   

Node:1 Color:1   Node:2 Color:3   Node:3 Color:3   Node:4 Color:3   Node:5 Color:3   

Node:1 Color:1   Node:2 Color:3   Node:3 Color:3   Node:4 Color:1   Node:5 Color:3   

Node:1 Color:1   Node:2 Color:3   Node:3 Color:3   Node:4 Color:1   Node:5 Color:1   

Node:1 Color:1   Node:2 Color:1   Node:3 Color:1   Node:4 Color:2   Node:5 Color:1   

Node:1 Color:1   Node:2 Color:1   Node:3 Color:1   Node:4 Color:2   Node:5 Color:2   

Node:1 Color:1   Node:2 Color:1   Node:3 Color:1   Node:4 Color:1   Node:5 Color:1   

Node:1 Color:1   Node:2 Color:1   Node:3 Color:3   Node:4 Color:2   Node:5 Color:1   

Node:1 Color:1   Node:2 Color:1   Node:3 Color:3   Node:4 Color:1   Node:5 Color:3   

Node:1 Color:1   Node:2 Color:1   Node:3 Color:3   Node:4 Color:1   Node:5 Color:1   

Node:1 Color:1   Node:2 Color:2   Node:3 Color:1   Node:4 Color:2   Node:5 Color:1   

Node:1 Color:1   Node:2 Color:2   Node:3 Color:1   Node:4 Color:2   Node:5 Color:2   

Node:1 Color:1   Node:2 Color:2   Node:3 Color:1   Node:4 Color:3   Node:5 Color:2   

Node:1 Color:1   Node:2 Color:2   Node:3 Color:2   Node:4 Color:2   Node:5 Color:2   

Node:1 Color:1   Node:2 Color:2   Node:3 Color:2   Node:4 Color:3   Node:5 Color:3   

Node:1 Color:1   Node:2 Color:2   Node:3 Color:2   Node:4 Color:3   Node:5 Color:2   

Node:1 Color:2   Node:2 Color:2   Node:3 Color:2   Node:4 Color:3   Node:5 Color:2   

Node:1 Color:2   Node:2 Color:2   Node:3 Color:2   Node:4 Color:3   Node:5 Color:2   

Node:1 Color:2   Node:2 Color:2   Node:3 Color:2   Node:4 Color:1   Node:5 Color:2   

Node:1 Color:2   Node:2 Color:2   Node:3 Color:3   Node:4 Color:3   Node:5 Color:2   

Node:1 Color:2   Node:2 Color:2   Node:3 Color:3   Node:4 Color:1   Node:5 Color:2   

Node:1 Color:2   Node:2 Color:2   Node:3 Color:3   Node:4 Color:1   Node:5 Color:1   

Node:1 Color:2   Node:2 Color:1   Node:3 Color:3   Node:4 Color:1   Node:5 Color:1   

Node:1 Color:2   Node:2 Color:1   Node:3 Color:3   Node:4 Color:1   Node:5 Color:2   

Node:1 Color:2   Node:2 Color:1   Node:3 Color:3   Node:4 Color:1   Node:5 Color:1   

Node:1 Color:2   Node:2 Color:1   Node:3 Color:3   Node:4 Color:1   Node:5 Color:1   

Node:1 Color:2   Node:2 Color:1   Node:3 Color:3   Node:4 Color:1   Node:5 Color:2   

Node:1 Color:2   Node:2 Color:1   Node:3 Color:3   Node:4 Color:1   Node:5 Color:1   

Node:1 Color:2   Node:2 Color:2   Node:3 Color:3   Node:4 Color:1   Node:5 Color:1   

Node:1 Color:2   Node:2 Color:2   Node:3 Color:3   Node:4 Color:1   Node:5 Color:2   

Node:1 Color:2   Node:2 Color:2   Node:3 Color:3   Node:4 Color:3   Node:5 Color:2   

Node:1 Color:2   Node:2 Color:2   Node:3 Color:2   Node:4 Color:1   Node:5 Color:2   

Node:1 Color:2   Node:2 Color:2   Node:3 Color:2   Node:4 Color:3   Node:5 Color:2   

Node:1 Color:2   Node:2 Color:2   Node:3 Color:2   Node:4 Color:3   Node:5 Color:2   

Node:1 Color:3   Node:2 Color:2   Node:3 Color:1   Node:4 Color:2   Node:5 Color:2   

Node:1 Color:3   Node:2 Color:2   Node:3 Color:1   Node:4 Color:2   Node:5 Color:2   

Node:1 Color:3   Node:2 Color:2   Node:3 Color:1   Node:4 Color:1   Node:5 Color:2   

Node:1 Color:3   Node:2 Color:2   Node:3 Color:3   Node:4 Color:2   Node:5 Color:2   

Node:1 Color:3   Node:2 Color:2   Node:3 Color:3   Node:4 Color:1   Node:5 Color:2   

Node:1 Color:3   Node:2 Color:2   Node:3 Color:3   Node:4 Color:1   Node:5 Color:3   

Node:1 Color:3   Node:2 Color:3   Node:3 Color:3   Node:4 Color:1   Node:5 Color:3   

Node:1 Color:3   Node:2 Color:3   Node:3 Color:3   Node:4 Color:1   Node:5 Color:2   

Node:1 Color:3   Node:2 Color:3   Node:3 Color:3   Node:4 Color:1   Node:5 Color:3   

Node:1 Color:3   Node:2 Color:3   Node:3 Color:3   Node:4 Color:1   Node:5 Color:3   

Node:1 Color:3   Node:2 Color:3   Node:3 Color:3   Node:4 Color:1   Node:5 Color:2   

Node:1 Color:3   Node:2 Color:3   Node:3 Color:3   Node:4 Color:1   Node:5 Color:3   

Node:1 Color:3   Node:2 Color:2   Node:3 Color:3   Node:4 Color:1   Node:5 Color:3   

Node:1 Color:3   Node:2 Color:2   Node:3 Color:3   Node:4 Color:1   Node:5 Color:2   

Node:1 Color:3   Node:2 Color:2   Node:3 Color:3   Node:4 Color:2   Node:5 Color:2   

Node:1 Color:3   Node:2 Color:2   Node:3 Color:1   Node:4 Color:1   Node:5 Color:2   

Node:1 Color:3   Node:2 Color:2   Node:3 Color:1   Node:4 Color:2   Node:5 Color:2   

Node:1 Color:3   Node:2 Color:2   Node:3 Color:1   Node:4 Color:2   Node:5 Color:2   

Node:1 Color:1   Node:2 Color:2   Node:3 Color:1   Node:4 Color:2   Node:5 Color:2   

Node:1 Color:1   Node:2 Color:2   Node:3 Color:1   Node:4 Color:2   Node:5 Color:1   

Node:1 Color:1   Node:2 Color:2   Node:3 Color:1   Node:4 Color:3   Node:5 Color:2   

Node:1 Color:1   Node:2 Color:2   Node:3 Color:2   Node:4 Color:2   Node:5 Color:2   

Node:1 Color:1   Node:2 Color:2   Node:3 Color:2   Node:4 Color:3   Node:5 Color:2   

Node:1 Color:1   Node:2 Color:2   Node:3 Color:2   Node:4 Color:3   Node:5 Color:3   

Node:1 Color:1   Node:2 Color:3   Node:3 Color:3   Node:4 Color:1   Node:5 Color:3   

Node:1 Color:1   Node:2 Color:3   Node:3 Color:3   Node:4 Color:1   Node:5 Color:1   

Node:1 Color:1   Node:2 Color:3   Node:3 Color:3   Node:4 Color:3   Node:5 Color:3   

Node:1 Color:1   Node:2 Color:3   Node:3 Color:2   Node:4 Color:1   Node:5 Color:3   

Node:1 Color:1   Node:2 Color:3   Node:3 Color:2   Node:4 Color:3   Node:5 Color:2   

Node:1 Color:1   Node:2 Color:3   Node:3 Color:2   Node:4 Color:3   Node:5 Color:3   

Node:1 Color:1   Node:2 Color:1   Node:3 Color:3   Node:4 Color:1   Node:5 Color:3   

Node:1 Color:1   Node:2 Color:1   Node:3 Color:3   Node:4 Color:1   Node:5 Color:1   

Node:1 Color:1   Node:2 Color:1   Node:3 Color:3   Node:4 Color:2   Node:5 Color:1   

Node:1 Color:1   Node:2 Color:1   Node:3 Color:1   Node:4 Color:1   Node:5 Color:1   

Node:1 Color:1   Node:2 Color:1   Node:3 Color:1   Node:4 Color:2   Node:5 Color:2   

Node:1 Color:1   Node:2 Color:1   Node:3 Color:1   Node:4 Color:2   Node:5 Color:1   

BUILD SUCCESSFUL (total time: 16 minutes 49 seconds)
*/