using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using iTextSharp.text;
using iTextSharp.text.pdf;

namespace ProjectBase.Utils
{
    public class PdfMaker
    {

        public static byte[] MergePdfFiles(List<byte[]> pdfInputs)
        {
            using (MemoryStream stream = new MemoryStream())
            {
                Document document = new Document();
                PdfWriter writer = PdfWriter.GetInstance(document, stream);
                document.Open();
                PdfContentByte cb = writer.DirectContent;
                PdfImportedPage newPage;
                PdfReader reader;

                foreach (var p in pdfInputs)
                {
                    reader = new PdfReader(p);
                    int iPageNum = reader.NumberOfPages;
                    for (int i = 1; i <= iPageNum; i++)
                    {
                        document.NewPage();
                        newPage = writer.GetImportedPage(reader, i);
                        cb.AddTemplate(newPage, 0, 0);
                    }
                }
                document.Close();
                writer.Close();
                return stream.ToArray();
            }
        }
    }
}
