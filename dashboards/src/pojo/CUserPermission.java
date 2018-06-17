package pojo;

public class CUserPermission {
	Integer permissionId;
	String name;
	boolean hasPermission;
	
	public CUserPermission(Integer permissionId, String name, boolean hasPermission) {
		super();
		this.permissionId = permissionId;
		this.name = name;
		this.hasPermission = hasPermission;
	}

	public Integer getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Integer permissionId) {
		this.permissionId = permissionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isHasPermission() {
		return hasPermission;
	}

	public void setHasPermission(boolean hasPermission) {
		this.hasPermission = hasPermission;
	}
	
	
}
