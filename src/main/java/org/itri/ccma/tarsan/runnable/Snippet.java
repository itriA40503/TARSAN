package org.itri.ccma.tarsan.runnable;

// Just for testing purpose (testing some simple logic in java applications)
public class Snippet {
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Snippet s = new Snippet();
		String str = "http://140.96.29.240:8080/TARSAN/service/main?service=createUser&jsonPara=[(%22account%22:%22test_user_8%22),(%22password%22:\"\"\"\"%22%22%22%22%22%22%22%22%22\")]";
		System.out.println(str);
		str = str.replaceAll("(\\%22|\\\"){2,}", "%22%22");
		System.out.println(str);
	}
}
