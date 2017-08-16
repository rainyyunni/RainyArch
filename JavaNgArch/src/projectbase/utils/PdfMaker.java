package projectbase.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;

import projectbase.data.UtilQuery;

public class PdfMaker
{

    public static byte[] MergePdfFiles(List<byte[]> pdfInputs) throws DocumentException, IOException
    {
    	ByteArrayOutputStream stream = new ByteArrayOutputStream();

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, stream);
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            PdfImportedPage newPage;
            PdfReader reader;

            for (byte[] p : pdfInputs)
            {
                reader = new PdfReader(p);
                int iPageNum = reader.getNumberOfPages();
                for (int i = 1; i <= iPageNum; i++)
                {
                    document.newPage();
                    newPage = writer.getImportedPage(reader, i);
                    cb.addTemplate(newPage, 0, 0);
                }
            }
            document.close();
            writer.close();
            return stream.toByteArray();

    }

    public static byte[] AddStamp(byte[] orgPdf,byte[] stamp,boolean stampIsImg) throws Exception { 

    	PdfReader readerOrg = new PdfReader(orgPdf); 
    	int n = readerOrg.getNumberOfPages(); 
    	if(n<1) throw new RuntimeException("PDF文件空");
    	
    	ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
    	PdfStamper stamper = new PdfStamper(readerOrg,  
    			resultStream); 
    	
   	
    	if(stampIsImg){
        	Image stampImg = Image.getInstance(stamp); 
        	stampImg.setAbsolutePosition(0, 0); 
        	stampImg.setAlignment(Image.ALIGN_LEFT); 
        	for(int i=1;i<=n;i++){
    	    	PdfContentByte under= stamper.getUnderContent(i); // stamp under the existing page 
    	    	under.addImage(stampImg); 
        	}
    	}else{
    		PdfReader stampPdf = new PdfReader(stamp); 
    		if(stampPdf.getNumberOfPages()<1) throw new RuntimeException("签章文件空");
        	for(int i=1;i<=n;i++){
    	    	PdfContentByte under= stamper.getUnderContent(i); // stamp under the existing page 
    	    	under.addTemplate(stamper.getImportedPage(stampPdf, 1), 0, 0);
        	}
    		
    	}

    	stamper.close(); 
    	return resultStream.toByteArray();
    } 
    
    public static void main(String[] args) throws Exception { 
    	byte[] t=Files.readAllBytes(Paths.get("c:\\Work\\stamp.jpg"));
    	Hashtable<String,Object> p=new Hashtable<String,Object>();
    	p.put("t", t);
    	
    	UtilQuery.StatelessExecuteSql("update gn_corp set stamp=:t where id=1",p);
    	byte[] stamp=(byte[])UtilQuery.StatelessGetScalarBySql("select stamp from gn_corp where id=1");
    	
    	byte[] orgPdf=Files.readAllBytes(Paths.get("C:\\Work\\1.pdf"));
    	//byte[] stamp=Files.readAllBytes(Paths.get("c:\\Work\\stamp.jpg"));
    	byte[] bytes=AddStamp(orgPdf,stamp,true);
    	//byte[] stamp=Files.readAllBytes(Paths.get("c:\\Work\\stamp.pdf"));
    	//byte[] bytes=AddStamp(orgPdf,stamp,false); 
    	FileOutputStream f=new FileOutputStream("C:\\Work\\text1.pdf");
    	f.write(bytes);
    	f.close();
    } 
}
