package com.ruyicai.charge.dna.v2.thirdpart.jaws;

/** WebService　服务接口客户端代理类。
 * 
 * @author Administrator
 */
public class IOrderServerWSProxy implements com.ruyicai.charge.dna.v2.thirdpart.jaws.IOrderServerWS {
  private String _endpoint = null;
  private String _namespace = null;
  private com.ruyicai.charge.dna.v2.thirdpart.jaws.IOrderServerWS iOrderServerWS = null;
  
//  public IOrderServerWSProxy() {
//    //_initIOrderServerWSProxy();
//  }
  
  /** 类构造器
   * 
   * @param endpoint　服务地址
   * @param namespace 服务名称
   */
  public IOrderServerWSProxy(String endpoint, String namespace) {
	  this.setEndpoint(endpoint);
	  this.setNamespace(namespace);
	  _initIOrderServerWSProxy();
	  }
  
  private void _initIOrderServerWSProxy() {
    try {
      iOrderServerWS =  new com.ruyicai.charge.dna.v2.thirdpart.jaws.IOrderServerWSBindingStub(new java.net.URL(_endpoint), _namespace, null);//(new dnapay.service.thirdpart.jaws.OrderServerWSServiceLocator()).getOrderServerWSPort();
      if (iOrderServerWS != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)iOrderServerWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)iOrderServerWS)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    //catch (javax.xml.rpc.ServiceException serviceException) {serviceException.printStackTrace();}
    catch(Exception e){e.printStackTrace();}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (iOrderServerWS != null)
      ((javax.xml.rpc.Stub)iOrderServerWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.ruyicai.charge.dna.v2.thirdpart.jaws.IOrderServerWS getIOrderServerWS() {
    if (iOrderServerWS == null)
      _initIOrderServerWSProxy();
    return iOrderServerWS;
  }
  
  public com.ruyicai.charge.dna.v2.thirdpart.jaws.PosMessage transact(com.ruyicai.charge.dna.v2.thirdpart.jaws.PosMessage arg0) throws java.rmi.RemoteException{
    if (iOrderServerWS == null)
      _initIOrderServerWSProxy();
    return iOrderServerWS.transact(arg0);
  }

public String getNamespace() {
	return _namespace;
}

public void setNamespace(String namespace) {
	this._namespace = namespace;
}
  
  
}