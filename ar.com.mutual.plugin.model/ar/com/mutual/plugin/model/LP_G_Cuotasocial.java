/** Modelo Generado - NO CAMBIAR MANUALMENTE - Disytel */
package ar.com.mutual.plugin.model;
import org.openXpertya.model.*;
import java.util.logging.Level;
 import java.util.*;
import java.sql.*;
import java.math.*;
import org.openXpertya.util.*;
/** Modelo Generado por G_Cuotasocial
 *  @author Comunidad de Desarrollo Libertya*         *Basado en Codigo Original Modificado, Revisado y Optimizado de:*         * Jorg Janke 
 *  @version  - 2014-07-31 10:33:24.552 */
public class LP_G_Cuotasocial extends org.openXpertya.model.PO
{
/** Constructor estándar */
public LP_G_Cuotasocial (Properties ctx, int G_Cuotasocial_ID, String trxName)
{
super (ctx, G_Cuotasocial_ID, trxName);
/** if (G_Cuotasocial_ID == 0)
{
setAnio (0);
setC_BPartner_ID (0);
setfechapago (new Timestamp(System.currentTimeMillis()));
setG_Cuotasocial_ID (0);
setmes (0);
setmonto (Env.ZERO);
}
 */
}
/** Load Constructor */
public LP_G_Cuotasocial (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID */
public static final int Table_ID = M_Table.getTableID("G_Cuotasocial");

/** TableName=G_Cuotasocial */
public static final String Table_Name="G_Cuotasocial";

protected static KeyNamePair Model = new KeyNamePair(Table_ID,"G_Cuotasocial");
protected static BigDecimal AccessLevel = new BigDecimal(3);

/** Load Meta Data */
protected POInfo initPO (Properties ctx)
{
POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
return poi;
}
public String toString()
{
StringBuffer sb = new StringBuffer ("LP_G_Cuotasocial[").append(getID()).append("]");
return sb.toString();
}
/** Set Anio */
public void setAnio (int Anio)
{
set_Value ("Anio", new Integer(Anio));
}
/** Get Anio */
public int getAnio() 
{
Integer ii = (Integer)get_Value("Anio");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Business Partner .
Identifies a Business Partner */
public void setC_BPartner_ID (int C_BPartner_ID)
{
set_Value ("C_BPartner_ID", new Integer(C_BPartner_ID));
}
/** Get Business Partner .
Identifies a Business Partner */
public int getC_BPartner_ID() 
{
Integer ii = (Integer)get_Value("C_BPartner_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set fechapago */
public void setfechapago (Timestamp fechapago)
{
if (fechapago == null) throw new IllegalArgumentException ("fechapago is mandatory");
set_Value ("fechapago", fechapago);
}
/** Get fechapago */
public Timestamp getfechapago() 
{
return (Timestamp)get_Value("fechapago");
}
/** Set G_Cuotasocial_ID */
public void setG_Cuotasocial_ID (int G_Cuotasocial_ID)
{
set_ValueNoCheck ("G_Cuotasocial_ID", new Integer(G_Cuotasocial_ID));
}
/** Get G_Cuotasocial_ID */
public int getG_Cuotasocial_ID() 
{
Integer ii = (Integer)get_Value("G_Cuotasocial_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set mes */
public void setmes (int mes)
{
set_Value ("mes", new Integer(mes));
}
/** Get mes */
public int getmes() 
{
Integer ii = (Integer)get_Value("mes");
if (ii == null) return 0;
return ii.intValue();
}
/** Set monto */
public void setmonto (BigDecimal monto)
{
if (monto == null) throw new IllegalArgumentException ("monto is mandatory");
set_Value ("monto", monto);
}
/** Get monto */
public BigDecimal getmonto() 
{
BigDecimal bd = (BigDecimal)get_Value("monto");
if (bd == null) return Env.ZERO;
return bd;
}
}
