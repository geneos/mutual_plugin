/** Modelo Generado - NO CAMBIAR MANUALMENTE - Disytel */
package ar.com.mutual.plugin.model;
import org.openXpertya.model.*;
import java.util.logging.Level;
 import java.util.*;
import java.sql.*;
import java.math.*;
import org.openXpertya.util.*;
/** Modelo Generado por G_Parametros
 *  @author Comunidad de Desarrollo Libertya*         *Basado en Codigo Original Modificado, Revisado y Optimizado de:*         * Jorg Janke 
 *  @version  - 2014-07-31 10:33:24.711 */
public class LP_G_Parametros extends org.openXpertya.model.PO
{
/** Constructor estándar */
public LP_G_Parametros (Properties ctx, int G_Parametros_ID, String trxName)
{
super (ctx, G_Parametros_ID, trxName);
/** if (G_Parametros_ID == 0)
{
setG_Parametros_ID (0);
setName (null);
setValue (null);
}
 */
}
/** Load Constructor */
public LP_G_Parametros (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID */
public static final int Table_ID = M_Table.getTableID("G_Parametros");

/** TableName=G_Parametros */
public static final String Table_Name="G_Parametros";

protected static KeyNamePair Model = new KeyNamePair(Table_ID,"G_Parametros");
protected static BigDecimal AccessLevel = new BigDecimal(3);

/** Load Meta Data */
protected POInfo initPO (Properties ctx)
{
POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
return poi;
}
public String toString()
{
StringBuffer sb = new StringBuffer ("LP_G_Parametros[").append(getID()).append("]");
return sb.toString();
}
/** Set Description.
Optional short description of the record */
public void setDescription (String Description)
{
if (Description != null && Description.length() > 255)
{
log.warning("Length > 255 - truncated");
Description = Description.substring(0,255);
}
set_Value ("Description", Description);
}
/** Get Description.
Optional short description of the record */
public String getDescription() 
{
return (String)get_Value("Description");
}
/** Set G_Parametros_ID */
public void setG_Parametros_ID (int G_Parametros_ID)
{
set_ValueNoCheck ("G_Parametros_ID", new Integer(G_Parametros_ID));
}
/** Get G_Parametros_ID */
public int getG_Parametros_ID() 
{
Integer ii = (Integer)get_Value("G_Parametros_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Name.
Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name == null) throw new IllegalArgumentException ("Name is mandatory");
if (Name.length() > 255)
{
log.warning("Length > 255 - truncated");
Name = Name.substring(0,255);
}
set_Value ("Name", Name);
}
/** Get Name.
Alphanumeric identifier of the entity */
public String getName() 
{
return (String)get_Value("Name");
}
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(getID(), getName());
}
/** Set Search Key.
Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value == null) throw new IllegalArgumentException ("Value is mandatory");
if (Value.length() > 40)
{
log.warning("Length > 40 - truncated");
Value = Value.substring(0,40);
}
set_Value ("Value", Value);
}
/** Get Search Key.
Search key for the record in the format required - must be unique */
public String getValue() 
{
return (String)get_Value("Value");
}
}
