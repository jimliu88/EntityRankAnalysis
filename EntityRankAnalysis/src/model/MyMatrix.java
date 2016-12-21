package model;

import java.util.ArrayList;
import java.util.List;

public class MyMatrix {

	private List<double[]> matrix;
	private int list_length;
	private int array_length;
	
	public MyMatrix(int list_length,int array_length){
		System.out.println(array_length);
		this.list_length=list_length;
		this.array_length=array_length;
		matrix=new ArrayList<double[]>();
		for(int index=0;index<list_length;index++){
			double[] array=new double[array_length];
			matrix.add(array);
		}
		
		
	}
	
	public MyMatrix(int list_length, int array_length, double value){
		this.list_length=list_length;
		this.array_length=array_length;
		matrix=new ArrayList<double[]>();
		for(int index_list=0;index_list<list_length;index_list++){
			double[] array=new double[array_length];
			for(int index_array=0;index_array<array.length;index_array++){
				array[index_array]=value;
			}
			matrix.add(array);
		}		
	}
	
	public void set(int index_in_list, int index_in_array, double value){
		matrix.get(index_in_list)[index_in_array]=value;
	}
	
	public double get(int index_in_list, int index_in_array){
		return matrix.get(index_in_list)[index_in_array];
	}
	
	public int getRowSize(){
		return this.list_length;
	}
	
	public int getColSize(){
		return this.array_length;
	}
	
	public double[] getRowArray(int row_index_in_list){
		return matrix.get(row_index_in_list);
	}
	
	public double[] getColArray(int col_index_in_list){
		double[] col_array_at_index=new double[list_length];
		for(int i=0;i<col_array_at_index.length;i++){
			col_array_at_index[i]=matrix.get(i)[col_index_in_list];
		}
		return col_array_at_index;
	}
	
	public MyMatrix times (MyMatrix B) {
	      if (B.list_length != array_length) {
	         throw new IllegalArgumentException("Matrix inner dimensions must agree.");
	      }
	      MyMatrix X = new MyMatrix(list_length,B.array_length);
	      MyMatrix result=new MyMatrix(list_length, B.array_length);
	      for(int index_row=0;index_row<list_length;index_row++){
	    	  for(int index_col=0;index_col<B.array_length;index_col++){
	    		  double value=calArrayTimes(matrix.get(index_row),B.getColArray(index_col));
	    		  result.set(index_row, index_col, value);
	    	  }
	      }
	      
	     return result; 
	   }
	
	public MyMatrix times(double value){
		MyMatrix result=new MyMatrix(list_length, array_length);
		for(int index_row=0;index_row<result.list_length;index_row++){
	    	  for(int index_col=0;index_col<result.array_length;index_col++){
	    		  double new_value=matrix.get(index_row)[index_col]*value;
	    		  result.set(index_row, index_col, new_value);
	    	  }
	      }
	    return result;
	}
	
	public MyMatrix plus(MyMatrix B){
		checkMatrixDimensions(B);
		MyMatrix result=new MyMatrix(list_length, array_length);
		for(int index_row=0;index_row<result.list_length;index_row++){
	    	  for(int index_col=0;index_col<result.array_length;index_col++){
	    		  double new_value=matrix.get(index_row)[index_col]+B.get(index_row, index_col);
	    		  result.set(index_row, index_col, new_value);
	    	  }
	      }
	    return result;
	}
	
	public double calArrayTimes(double[] A, double[] B){
		if (A.length!=B.length) {
	         throw new IllegalArgumentException("Matrix inner dimensions must agree.");
	      }
		int length=A.length;
		double sum=0;
		for(int index=0;index<length;index++){
			sum+=A[index]*B[index];
		}
		
		return sum;
	}
	
	 /** Check if size(A) == size(B) **/

	   private void checkMatrixDimensions (MyMatrix B) {
	      if (B.list_length!= list_length || B.array_length != array_length) {
	         throw new IllegalArgumentException("Matrix dimensions must agree.");
	      }
	   }
}

