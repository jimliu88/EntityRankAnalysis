package analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Map.Entry;

import Jama.Matrix;
import Util.MapUtil;
import model.Entity;
import model.MyMatrix;

public class EntityRank {

	private  double ALPHA; // = 0.85;
	private  double DISTANCE; // = 0.0000001;
	private HashMap<String, Double> entity_rank_result = new LinkedHashMap<String, Double>();
	/**
	 * 
	 * @param damp: entity rank
	 * @param iteration_threshold: entity rank
	 */
	public EntityRank(double damp, double iteration_threshold){
		this.ALPHA=damp;
		this.DISTANCE=damp;
	}
	
	@SuppressWarnings({ "resource", "rawtypes" })
	public MyMatrix generateEntityLinkArray(String file_name, String link_flag) {
		if (file_name != null && !file_name.isEmpty()) {
			File file = new File(file_name);
			HashMap<String, Entity> entity_map = new LinkedHashMap<>();
			try {
				InputStreamReader input_stream_reader = new InputStreamReader(new FileInputStream(file),"utf-8");
				BufferedReader buffered_reader = new BufferedReader(input_stream_reader);
				String buffered_line = null;
				while ((buffered_line = buffered_reader.readLine()) != null) {
					if (!buffered_line.isEmpty() && buffered_line.split(link_flag).length > 1) {
						String first_entity_name = buffered_line.split(link_flag)[0];
						String second_entity_name = buffered_line.split(link_flag)[1];
						if (!entity_map.containsKey(first_entity_name)) {
							Entity entity = new Entity();
							entity.setName(first_entity_name);
							HashMap<String, Integer> link_to_entities = new HashMap<String, Integer>();
							link_to_entities.put(second_entity_name, 1);
							entity.setLinkToEntitySet(link_to_entities);
							entity_map.put(first_entity_name, entity);
						} else {
							Entity entity = entity_map.get(first_entity_name);
							if (!entity.getLinkToEntitySet().containsKey(second_entity_name)) {
								entity.getLinkToEntitySet().put(second_entity_name, 1);
							} else {
								int new_value = entity.getLinkToEntitySet().get(second_entity_name) + 1;
								entity.getLinkToEntitySet().put(second_entity_name, new_value);
							}
							entity_map.put(first_entity_name, entity);
						}

						if (!entity_map.containsKey(second_entity_name)) {
							Entity entity = new Entity();
							entity.setName(second_entity_name);
							HashMap<String, Integer> link_to_entities = new HashMap<String, Integer>();
							entity.setLinkToEntitySet(link_to_entities);
							entity_map.put(second_entity_name, entity);
						}
					}

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int array_length = entity_map.size();
			System.out.println(array_length);			
			//Matrix initial_entity_link_matrix=new Matrix(array_length,array_length);
					//new Matrix(array_length,array_length);
			
			/* init entity binary array */
			MyMatrix initial_entity_link_matrix=new MyMatrix(array_length,array_length);
			
			
			Iterator iterator = entity_map.entrySet().iterator();
			int postion_row = 0;
			while (iterator.hasNext()) {
				Entry entry = (Entry) iterator.next();
				Entity link_from_entity = (Entity) entry.getValue();
				entity_rank_result.put(entry.getKey().toString(), (double) 0);
				int time = link_from_entity.getLinkToEntitySet().size();
				int postion_column = 0;
				Iterator iterator_tmp = entity_map.entrySet().iterator();
				while (postion_column < array_length && iterator_tmp.hasNext()) {
					if (time == 0) {
						initial_entity_link_matrix.set(postion_row, postion_column, 0);
					} else {
						Entry entry_temp = (Entry) iterator_tmp.next();
						String entity_item = (String) entry_temp.getKey();
						if (link_from_entity.getLinkToEntitySet().containsKey(entity_item)) {
							double value= (double) link_from_entity
									.getLinkToEntitySet().get(entity_item)
									/ link_from_entity.getLinkToEntitySet().size();
							initial_entity_link_matrix.set(postion_row, postion_column, value);							
							time--;
						}
					}
					postion_column++;
				}
				postion_row++;
			}						
			return initial_entity_link_matrix;
		}
		return null;
	}
	
	/***
	 * 
	 * @param entity_link_array
	 * @return
	 */
	public Matrix generateEntityLinkMatrix(double [][] entity_link_array){
		Matrix martix=new Matrix(entity_link_array);
		return martix;
	}
	
	public MyMatrix generateInitialEntityRankMatrix(int size){
		Random random = new Random();
		MyMatrix initial_entity_rank_matrix=new MyMatrix(size,1);		
		for (int i = 0; i < size; i++) {
			initial_entity_rank_matrix.set(i, 0, new Double(5 * random.nextDouble()));
		}
		
		return initial_entity_rank_matrix;
	}
	
	public double calculateVectorDistance(double [] vector_x, double [] vector_y){
		double distance = 0;
		if (vector_x.length != vector_y.length) {
			return -1;
		}
		for (int i = 0; i < vector_x.length; i++) {
			distance += Math.pow(vector_x[i] - vector_y[i], 2);
		}
		return Math.sqrt(distance);
	}
	
	
	/***
	 * 
	 * @param entity_link_array
	 * @param iterator_count, 
	 * @return
	 */
	public MyMatrix calculateEntityRank(MyMatrix initial_entity_link_matrix, int iterator_count){
		int size=initial_entity_link_matrix.getRowSize();
		
		//initial_entity_link_matrix.print(5, 15);
		MyMatrix initial_entity_rank_matrix=generateInitialEntityRankMatrix(size);
		
		MyMatrix iterator_entity_rank_matrix=null;
		
		MyMatrix iteraror_primary_matrix=new MyMatrix(size,1,(double)1/size);

		int count=0;
		if(iterator_count-->0){
			while(iterator_count>0){
				iterator_entity_rank_matrix=
						initial_entity_link_matrix.times(initial_entity_rank_matrix).times(ALPHA).plus(iteraror_primary_matrix.times(1-ALPHA));
				boolean iterator_stop=
						calculateVectorDistance(iterator_entity_rank_matrix.getColArray(0),initial_entity_rank_matrix.getColArray(0))>DISTANCE?false:true;
				if(!iterator_stop){
					initial_entity_rank_matrix=iterator_entity_rank_matrix;				
				}else{
					break;
				}
				count++;
			}
			
		}else{
			while(true){
				iterator_entity_rank_matrix=
						initial_entity_link_matrix.times(initial_entity_rank_matrix).times(ALPHA).plus(iteraror_primary_matrix.times(1-ALPHA));
				boolean iterator_stop=
						calculateVectorDistance(iterator_entity_rank_matrix.getColArray(0),initial_entity_rank_matrix.getColArray(0))>DISTANCE?false:true;
				if(!iterator_stop){
					initial_entity_rank_matrix=iterator_entity_rank_matrix;				
				}else{
					break;
				}
				count++;
			}
		}	
		System.out.println(count);
		return iterator_entity_rank_matrix;
	}
	
	
	@SuppressWarnings({ "rawtypes"})
	public void runEntityRank(String file_name,String result_file, String link_flag, int iterator_count){
		MyMatrix rankResult=calculateEntityRank(generateEntityLinkArray(file_name, link_flag), iterator_count);
			 Iterator iterator=entity_rank_result.entrySet().iterator();
			 int index=0;
			 while(index < rankResult.getRowSize() && iterator.hasNext()){
				 Entry entry=(Entry) iterator.next();
				 entity_rank_result.put(entry.getKey().toString(), rankResult.get(index, 0));
				 index++;
			 }			 
			 entity_rank_result=MapUtil.sortByValue(entity_rank_result);	
			try {
				 OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(result_file), "GBK");
				 BufferedWriter pw = new BufferedWriter(osw);
				 Iterator iterator_tmp=entity_rank_result.entrySet().iterator();
				 while(iterator_tmp.hasNext()){
					 Entry entry_tmp=(Entry)iterator_tmp.next();
					 pw.append(entry_tmp.getKey()+":\t"+String.format("%.15f", (double)entry_tmp.getValue())+"\n");
					 pw.flush();
				 }
				 pw.close();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 

	}
}
