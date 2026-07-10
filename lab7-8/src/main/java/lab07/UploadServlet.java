package lab07;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@WebServlet("/UploadServlet")
public class UploadServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int LIMIAR_TAMANHO = 1024 * 1024; // 1MB

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        if (!ServletFileUpload.isMultipartContent(request)) {
            out.println("<p class='result-msg error'>Formulario deve ter enctype='multipart/form-data'.</p>");
            return;
        }

        String diretorio = getServletContext().getRealPath("/arquivos");
        File dir = new File(diretorio);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(LIMIAR_TAMANHO);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);

        try {
            List<FileItem> items = upload.parseRequest(request);

            int count = 0;
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    String nomeArquivo = new File(item.getName()).getName();
                    if (nomeArquivo != null && !nomeArquivo.isEmpty()) {
                        File arquivoSalvo = new File(diretorio, nomeArquivo);
                        item.write(arquivoSalvo);
                        count++;
                        out.println("<p class='result-msg success'>Arquivo \"" + nomeArquivo
                                + "\" enviado com sucesso (" + arquivoSalvo.getAbsolutePath() + ")</p>");
                    }
                }
            }

            if (count == 0) {
                out.println("<p class='result-msg info'>Nenhum arquivo foi selecionado.</p>");
            } else {
                out.println("<p><a class='btn primary' href='downloads.jsp'><i class='ti ti-download'></i> Ver arquivos</a></p>");
            }

        } catch (Exception e) {
            out.println("<p class='result-msg error'>Erro no upload: " + e.getMessage() + "</p>");
        }

        out.println("<p><a class='btn' href='upload.jsp'><i class='ti ti-upload'></i> Novo upload</a></p>");
    }
}
