package cse3040;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Item{
	private String item;
	private int count;
	public Item(String item)
	{
		this.item=item;
		this.count=1;
	}
	
	public String getItem() {
		return item;
	}
	public int getCount()
	{
		return count;
	}
	public void setCount(int count)
	{
		this.count=count;
	}
	public String toString()
	{
		return this.item+" "+this.count;
	}
}
class MyFileReader{
	public static void addToList(ArrayList<Item> list, String word)
	{
		int flag=1;
		String convertedWord="";
		for(int i=0; i<word.length(); i++)
		{
			char temp=word.charAt(i);
			if(temp<='Z' && temp>='A')//�빮�ڸ� �ҹ��ڷ� convert ����
			{
				temp=(char)(temp+'a'-'A');
			}
			convertedWord+=temp;
		}
		for(Item item:list)
		{
			if(convertedWord.equals(item.getItem()))//�̹� ����Ʈ �ȿ������� ī��Ʈ�� �ٲ���.
			{
				flag=0;
				item.setCount(item.getCount()+1);
				break;
			}
		}
		if(flag==1)//����Ʈ �ȿ� ���ٸ� ���ο� �������� ���� �߰�����.
		{
			Item item=new Item(convertedWord);
			list.add(item);
		}
	}
	public static boolean readDataFromFile(String filename, ArrayList<Item> list)
	{
		BufferedReader file=null;
		try {
			file=new BufferedReader(new FileReader(filename));
			while(true)
			{
				String str=file.readLine();
				String word="";
				if(str==null)
					break;
				for(int i=0; i<str.length(); i++)
				{
					if(str.charAt(i)==' ')//����
					{
						if(!word.equals(""))//word�� no letter�� �ƴ϶��
							addToList(list, word);//list�� �߰�����
						word="";//list�� �߰����� ���Ŀ��� ���� word�� �ֱ����� �ٽ� word=""�� �ٲپ���.
					}
					else
						word+=str.charAt(i);
				}
				if(!word.equals(""))//������ word�ֱ� ����. ������ word�� �� ���ǿ� �� �ɸ��Ƿ�
					addToList(list, word);//list�� �߰�����
			}
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}finally {
			try {
				file.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return false;
			}
		}
		return true;
	}
}


public class Problem15 { 
	public static void main(String[] args) { 
		ArrayList<Item> list = new ArrayList<>(); 
		boolean rv = MyFileReader.readDataFromFile("input_prob15.txt", list); 
		if(rv == false) { 
			System.out.println("Input file not found."); 
			return; 
		} 
		for(Item it: list) System.out.println(it); 
	} 
}

