//Copyright (c) 2014 sena. All rights reserved.
//タッチポイントを求めるクラス

package touchandpost.pak;
import java.util.ArrayList;

public class Figure {
	int r = 10;
	int g = 10;
	int b = 10;
	int a = 10;
    ArrayList<Integer> x = new ArrayList<Integer>();
    ArrayList<Integer> y = new ArrayList<Integer>();
	
    Figure(){
	}
    
    public int getR() {
    	return r;
    }
    
    public void setR(int i) {
    	this.r = i;
    }
    
    public int getG() {
    	return g;
    }
    
    public void setG(int i) {
    	this.g = i;
    }
    
    public int getB() {
    	return b;
    }
    
    public void setB(int i) {
    	this.b = i;
    }
    
    public int getA() {
    	return a;
    }
    
    public void setA(int i) {
    	this.a = i;
    }
    
	public ArrayList<Integer> getX() {
		return x;
	}

	public void setX(int x) {
		this.x.add(x);
	}

	public ArrayList<Integer> getY() {
		return y;
	}

	public void setY(int y) {
		this.y.add(y);
	}
}
