package ar.com.mutual.plugin.model;
 
import java.math.BigDecimal;
import java.util.Properties;

import org.openXpertya.model.MInOut;
import org.openXpertya.model.MInOutLine;
import org.openXpertya.model.MMovementLine;
import org.openXpertya.model.MStorage;
import org.openXpertya.model.PO;
import org.openXpertya.plugin.MPluginDocAction;
import org.openXpertya.plugin.MPluginStatusDocAction;
import org.openXpertya.process.DocAction;

 
public class MMovement extends MPluginDocAction {
 
 
	public MMovement(PO po, Properties ctx, String trxName, String aPackage) {
		super(po, ctx, trxName, aPackage);
		// TODO Auto-generated constructor stub
	}
	
	/**
	* Ejecución previa al prepareIt
	* 
	* @return estado del procesamiento
	*/
	
	public MPluginStatusDocAction prePrepareIt(DocAction document) {
		
		System.out.println("Entra al prePrepareIt");
		
		org.openXpertya.model.MMovement mov = (org.openXpertya.model.MMovement) document;
		MMovementLine[] lines = mov.getLines(true);
		boolean error = false;
		StringBuffer msg = new StringBuffer();
		for (MMovementLine aLine : lines) {
			
			System.out.println("Entra a las lineas");
			
			MStorage storage = MStorage.get(mov.getCtx(), aLine.getM_Locator_ID(), aLine.getM_Product_ID(), aLine.getM_AttributeSetInstance_ID(),
					mov.get_TrxName());
			
	        if( storage == null ) {
	        	System.out.println("Entra a storage null");
				error = true;
				org.openXpertya.model.MProduct product = new org.openXpertya.model.MProduct(mov.getCtx(),aLine.getM_Product_ID(),mov.get_TrxName());
				msg.append("Linea "+aLine.getLine()+", Producto: "+product.getValue()+" ==> Sin Stock, Necesario: "+aLine.getMovementQty());
				msg.append("\n");
	        } else {
	        	System.out.println("Entra a storage <> null");
				BigDecimal qtyAvailable = storage.getQtyOnHand().subtract(storage.getQtyReserved());
				if (qtyAvailable.compareTo(aLine.getMovementQty()) == -1) {
					System.out.println("Entra a storage y valida cantidades");
					error = true;
					org.openXpertya.model.MProduct product = new org.openXpertya.model.MProduct(mov.getCtx(),aLine.getM_Product_ID(),mov.get_TrxName());
					msg.append("Linea "+aLine.getLine()+", Producto: "+product.getValue()+" ==> Disponible:"+qtyAvailable+", Necesario: "+aLine.getMovementQty());
					msg.append("\n");
				}	        	
	        }
		}
		if (error) {
			status_docAction.setContinueStatus(0);
			status_docAction.setDocStatus(org.openXpertya.model.MMovement.DOCSTATUS_Invalid);
			status_docAction.setProcessMsg("Movimiento Inválido:\n"+msg);
		}
		return status_docAction;
	}	
}