package zou_z_l.weighted_doxy;

import org.dom4j.Element;
import org.dom4j.Text;
import org.dom4j.Document;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

import javax.xml.transform.Templates;

import java.util.List;


public class PutWeight {
	private static Stack<String> keyword_stack = new Stack<String>();
	private static Stack<Character> bracket_stack = new Stack<Character>();
	private static int count = 0;
	private static Map<String,Map<String,String>> circlemap = new HashMap<String,Map<String,String>>();
	//生成一个嵌套的map
	public static void weightMap(List limited_line) {
		XmlParser xmlparser = new XmlParser();
		PutWeight weightmap = new PutWeight();
		Map looptimes = new HashMap();
		Map weighted_map = new HashMap();
		Document document = xmlparser.load("/home/zou/zou_z_l/doxygen_output_of_ninegridimageview/xml/_nine_grid_image_view_8java.xml");
		org.dom4j.Element root = document.getRootElement();
		Element sonofroot = root.element("compounddef");
		Element codelines = sonofroot.element("programlisting");
		List<Element> linenumber = codelines.elements();
		for(Element e:linenumber) {
			//System.out.println(e.getName());
			for(Object a:limited_line) {
				if(a.equals(e.attributeValue("lineno"))) {
					ArrayList starttoend = xmlparser.startToEnd("/home/zou/zou_z_l/doxygen_output_of_ninegridimageview/xml/classcom_1_1jaeger_1_1ninegridimageview_1_1_nine_grid_image_view.xml","layoutChildrenView");
					List<Element> sonlist = e.elements();
					Element firstson = sonlist.get(0);
					count = weightmap.countSpace(firstson);
					weighted_map.put(e, count);
					looptimes.put(e, (int)0);
					circlemap.put(e.attributeValue("lineno"),new HashMap<String,String>());
					Map<String,String> inmap = circlemap.get(e.attributeValue("lineno"));
					weightmap.justweightit(xmlparser, a,e, count, starttoend, linenumber,weighted_map,looptimes,inmap);
					weightmap.nextConfig(a,inmap);
					System.out.println("这是keyword"+keyword_stack);         
					System.out.println("这是括号"+bracket_stack);  
				}
			}
		}
		System.out.println("circlemap:"+circlemap);
	}
	
	private void findbrackets(Object start, List<Element> sonlist,Element e, List<Element> linenumber){
		for(Element m:sonlist) {
			keywordMatch(m);
			String text = m.getText();
			bracketMatch(text);
		}	
	}
	
	private void nextConfig(Object a,Map<String, String> inmap) {
		int time = 0;
		for(String i:keyword_stack) {
			String tmp = String.valueOf(time);
			inmap.put(tmp, i);
			time+=1;
		}
		while(keyword_stack.isEmpty()!=true){
			   keyword_stack.pop();// 将数据弹出堆栈
			}
		while(bracket_stack.isEmpty()!=true){
			   bracket_stack.pop();// 将数据弹出堆栈
		}
	}
	
	
	private void justweightit(XmlParser xmlparser,Object a,Element e,int count,ArrayList starttoend,List<Element> linenumber,Map weighted_map,Map looptimes,Map inmap) {
		  Object start = starttoend.get(0);
		  String tmp = start.toString();
		  int line_num = Integer.parseInt(tmp);
		  boolean compare = true; 
		  while(compare) {
			  this.addition(starttoend,line_num, linenumber,e,weighted_map,looptimes,inmap);                                            
			  compare = this.compareNumber(line_num, a);
			  line_num = line_num+1;
		  }
		  
	}
	
	private boolean compareNumber(int line_num,Object a) {
		String m0 = String.valueOf(a);
		int min = Integer.parseInt(m0);
		if(line_num<=min) {
			//判断当前操作行是否在目标调用行之前
			return true;
		}else {
			return false;
		}
	}
	
	private int countSpace(Element firstson) {
		String xml_str = firstson.asXML();
		int count = 0;
		while(xml_str.indexOf("<sp/>")!=-1) {
			xml_str = xml_str.substring(xml_str.indexOf("<sp/>")+1,xml_str.length());
			count++;
		}
		//System.out.println("<sp/>出现的次数为:"+count+"次");
		return count;
	}
	
	private void addition(ArrayList starttoend,int line_num,List<Element> linenumber,Element e,Map weighted_map,Map looptimes,Map inmap) {
		int new_line_num = line_num+1;
		String new_line = String.valueOf(new_line_num);
		for(Element a:linenumber) {
			if(new_line.equals(a.attributeValue("lineno"))) {
				//System.out.println(a);
				List<Element> sonlist = a.elements();
				findbrackets(starttoend.get(0), sonlist, e, linenumber);

			}
		} 
	}
	

	private void bracketMatch(String text) {
		char[] bytes = text.toCharArray();
		for (int i = 1; i < bytes.length; ++i) {
			Character c1 = bytes[i];
			if ((c1.toString().equals("{"))){
				bracket_stack.push(c1);
			}else if (c1.toString().equals("}")) {
				bracket_stack.pop();
				keyword_stack.pop();
			}
		}
	}
	
	private void keywordMatch(Element i) {
		if(i.getText().equals("if")) {
			keyword_stack.push("if");
		}else if(i.getText().equals("for")){
			keyword_stack.push("for");
		}else if(i.getText().equals("while")){
			keyword_stack.push("while");
		}else if(i.getText().equals("switch")){
			keyword_stack.push("switch");
		}else if(i.getText().equals("else if")){
			keyword_stack.push("else if");
		}else if(i.getText().equals("else")){
			keyword_stack.push("else");
		}else if(i.getText().equals("try")){
			keyword_stack.push("try");
		}else if(i.getText().equals("finally")){
			keyword_stack.push("finally");
	}}

}










