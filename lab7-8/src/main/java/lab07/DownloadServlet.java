package lab07;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DownloadServlet")
public class DownloadServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String arquivo = request.getParameter("arquivo");
        String dirParam = request.getParameter("dir");

        if (arquivo == null || arquivo.trim().isEmpty()) {
            response.sendRedirect("vinhos.jsp");
            return;
        }

        arquivo = URLDecoder.decode(arquivo, StandardCharsets.UTF_8.name());

        String baseDir = getServletContext().getRealPath("/arquivos");
        String fullPath;
        if (dirParam != null && !dirParam.trim().isEmpty()) {
            fullPath = baseDir + File.separator + dirParam + File.separator + arquivo;
        } else {
            fullPath = baseDir + File.separator + arquivo;
        }

        File file = new File(fullPath);

        if (!file.exists() || file.isDirectory()) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println("<p class='result-msg error'>Arquivo nao encontrado.</p>");
            return;
        }

        String mimeType = getServletContext().getMimeType(file.getName());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        response.setContentType(mimeType);
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + file.getName() + "\"");

        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }
    }
}
