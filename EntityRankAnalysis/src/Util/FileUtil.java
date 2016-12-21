package Util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class FileUtil {

	private final static int MAXARRAYSIZE = 5000;

	public static void main(String args) {

		System.out.println("translation completed");
	}

	public ArrayList<List<String>> read(String file_name) {
		if (file_name != null && !file_name.isEmpty()) {
			ArrayList<List<String>> file_content_list = new ArrayList<List<String>>();
			File file = new File(file_name);
			if (!file.isDirectory()) {
				try {
					// System.out.println("开始加载文件......");
					InputStreamReader in_stream_reader = new InputStreamReader(new FileInputStream(file), "utf-8");
					BufferedReader buffer_reader = new BufferedReader(in_stream_reader);
					String line = null;
					int i = 0;
					try {
						while ((line = buffer_reader.readLine()) != null) {
							List<String> bufferLine = new ArrayList<String>();
							while (line != null && (++i) <= MAXARRAYSIZE) {
								if (!line.isEmpty()) {
									line = line.replaceAll(",", "，");
								}
								bufferLine.add(line);
								line = buffer_reader.readLine();
							}

							if (line != null && !line.isEmpty()) {
								file_content_list.add(bufferLine);
								i = i - MAXARRAYSIZE;
							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				for (File iterator_file : file.listFiles()) {
					read(iterator_file.getName());
				}
			}

			System.out.println("文件读取完成......");
			return file_content_list;
		}
		return null;
	}

	@SuppressWarnings("null")
	public void write(String file_name, StringBuffer content) {
		if (file_name != null || !file_name.isEmpty()) {
			File file = new File(file_name);
			if (file.exists())
				file.delete();
			// TODO
			try {
				OutputStreamWriter osWriter = new OutputStreamWriter(new FileOutputStream(file, true));
				BufferedWriter bWriter = new BufferedWriter(osWriter);
				try {
					bWriter.write(content.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					try {
						bWriter.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // not append

		}
	}

	/***
	 * 处理编码问题
	 * 
	 * @param file_name
	 * @return
	 * @throws IOException
	 */
	private String getCharset(String file_name) throws IOException {

		BufferedInputStream bin = new BufferedInputStream(new FileInputStream(file_name));
		int p = (bin.read() << 8) + bin.read();
		String code = null;
		switch (p) {
		case 0xefbb:
			code = "utf-8";
			break;
		case 0xfffe:
			code = "unicode";
			break;
		case 0xfeff:
			code = "utf-16be";
			break;
		default:
			code = "gbk";
		}
		return code;
	}

	
}
