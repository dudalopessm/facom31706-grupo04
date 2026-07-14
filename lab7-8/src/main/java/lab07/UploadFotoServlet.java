package lab07;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@WebServlet("/UploadFotoServlet")
public class UploadFotoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int LIMIAR_TAMANHO = 1024 * 1024;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        if (!ServletFileUpload.isMultipartContent(request)) {
            request.setAttribute("uploadMsg", "Formulario deve ter enctype='multipart/form-data'.");
            request.setAttribute("uploadTipo", "error");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/vinho-detalhe.jsp");
            dispatcher.forward(request, response);
            return;
        }

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(LIMIAR_TAMANHO);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);

        try {
            List<FileItem> items = upload.parseRequest(request);

            String idVinhoStr = null;
            for (FileItem item : items) {
                if (item.isFormField() && "idVinho".equals(item.getFieldName())) {
                    idVinhoStr = item.getString("UTF-8");
                    break;
                }
            }

            if (idVinhoStr == null) {
                request.setAttribute("uploadMsg", "Vinho nao especificado.");
                request.setAttribute("uploadTipo", "error");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/vinho-detalhe.jsp");
                dispatcher.forward(request, response);
                return;
            }

            int idVinho = Integer.parseInt(idVinhoStr);
            String diretorio = getServletContext().getRealPath("/arquivos/fotos/" + idVinho);
            File dir = new File(diretorio);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            int count = 0;
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    String nomeArquivo = new File(item.getName()).getName();
                    if (nomeArquivo != null && !nomeArquivo.isEmpty()) {
                        File arquivoSalvo = new File(diretorio, nomeArquivo);
                        item.write(arquivoSalvo);
                        count++;
                    }
                }
            }

            request.setAttribute("uploadMsg", count + " foto(s) enviada(s) com sucesso.");
            request.setAttribute("uploadTipo", count > 0 ? "success" : "info");

            RequestDispatcher dispatcher = request.getRequestDispatcher("/vinho-detalhe.jsp?id=" + idVinho);
            dispatcher.forward(request, response);

        } catch (Exception e) {
            request.setAttribute("uploadMsg", "Erro no upload: " + e.getMessage());
            request.setAttribute("uploadTipo", "error");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/vinho-detalhe.jsp");
            dispatcher.forward(request, response);
        }
    }
}
