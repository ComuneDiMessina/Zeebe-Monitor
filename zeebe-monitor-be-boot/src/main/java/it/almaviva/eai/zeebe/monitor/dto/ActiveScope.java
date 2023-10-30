package it.almaviva.eai.zeebe.monitor.dto;

import lombok.Data;

@Data
public class ActiveScope {
	
	  private long scopeKey;
	  private String scopeName;

	  public ActiveScope(long scopeKey, String scopeName) {
		    this.scopeKey = scopeKey;
		    this.scopeName = scopeName;
		  }
}
