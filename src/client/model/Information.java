package client.model;

public class Information
{
	private int id = -1;
	
	public Information(){}
	
	public Information(int id)
	{
		this.id = id;
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}