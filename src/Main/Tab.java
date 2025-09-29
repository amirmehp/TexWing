package Main;

public class Tab{
    public String name;
    public boolean changed;
    public String text;
    public String path;

    public Tab(String name, boolean changed, String text, String path){
	this.name = name;
	this.changed = changed;
	this.text = text;
	this.path = path;
    }
    public void print(){
	System.out.println(this.path);
	System.out.println(this.text);
    }
}


