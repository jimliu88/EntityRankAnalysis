package analysis;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String file_name=
				"C:\\Users\\ifchangesfsx\\Downloads\\companylinkanalysis-master\\companylinkanalysis-master\\data\\cv_corp_hop_records\\cv_corp_hop_records_small.txt";
		String link_flag="\t";
		int iterator_count=-1;
		String file_output="D:\\entity_rank_test.csv";
		
		double[][] entity_link_array = { { 0, 0.5, 0.5 }, { 0.5, 0, 0.5 }, { 0, 0, 0 } };
		String file_test_name="./data/data.txt";
	/*
		List<int[]> testlinkList=new LinkedList<int[]>();
		
		for(int i=0;i<800000;i++){
			int [] test_iner=new int[800000];
			testlinkList.add(test_iner);
		}
		*/
		
		
		
		EntityRank entity_rank = new EntityRank(0.85, 0.0000001);
		entity_rank.generateEntityLinkArray(file_name,link_flag);
		/*calculateEntityRank(entity_link_array, iterator_count);
		entity_rank.runEntityRank(args[0], args[1],link_flag, -1);		
		entity_rank.runEntityRank(file_name, file_output,link_flag, -1);	*/
	}

}
