package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.VinhoDAO;
import javaBeans.Cliente;
import javaBeans.UploadVinhoBean;
import javaBeans.Vinho;

@WebServlet("/admin/vinho")
public class VinhoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("clienteLogado") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        Cliente admin = (Cliente) session.getAttribute("clienteLogado");
        if (!"ADMIN".equals(admin.getTipo())) {
            response.sendRedirect(request.getContextPath() + "/loja.jsp");
            return;
        }

        String acao = request.getParameter("acao");
        String paginaErro = "cadastroVinho.jsp";
        if ("alterar".equals(acao)) {
            paginaErro = "editarVinho.jsp";
        }

        try {
            VinhoDAO vinhoDAO = new VinhoDAO();

            UploadVinhoBean upload = new UploadVinhoBean();
            upload.setDiretorio("images/vinhos");
            upload.setSize(2);
            upload.setExtensoesPermitidas("jpg,jpeg,png");

            if (!upload.processarUpload(request)) {
                request.setAttribute("erro", upload.getErro() != null ? upload.getErro() : "Erro ao processar upload.");
                request.getRequestDispatcher("/admin/" + paginaErro).forward(request, response);
                return;
            }

            int safra = Integer.parseInt(upload.getParametro("safra"));
            if (safra < 1900) {
                request.setAttribute("erro", "A safra deve ser maior ou igual a 1900.");
                request.getRequestDispatcher("/admin/" + paginaErro).forward(request, response);
                return;
            }

            Vinho vinho;
            int id;

            if ("alterar".equals(acao)) {
                id = Integer.parseInt(upload.getParametro("id"));
                vinho = vinhoDAO.buscarPorId(id);
                if (vinho == null) {
                    response.sendRedirect("vinhos.jsp");
                    return;
                }
            } else {
                vinho = new Vinho();
            }

            vinho.setNome(upload.getParametro("nome"));
            vinho.setSafra(safra);
            vinho.setDescricao(upload.getParametro("descricao"));
            vinho.setPreco(Double.parseDouble(upload.getParametro("preco")));
            vinho.setEstoque(Integer.parseInt(upload.getParametro("estoque")));
            vinho.setIdCategoria(Integer.parseInt(upload.getParametro("idCategoria")));

            if ("alterar".equals(acao)) {
                vinhoDAO.alterar(vinho);
            } else {
                id = vinhoDAO.inserir(vinho);
            }

            if (upload.temArquivo()) {
                if (upload.salvarArquivo(getServletContext(), id)) {
                    vinhoDAO.atualizarCaminhoFoto(id, "images/vinhos/" + id + ".jpg");
                } else {
                    request.setAttribute("aviso", "Vinho salvo, mas houve problema ao salvar a foto: " + upload.getErro());
                    request.getRequestDispatcher("/admin/" + paginaErro).forward(request, response);
                    return;
                }
            }

            response.sendRedirect("vinhos.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro: " + e.getMessage());
            request.getRequestDispatcher("/admin/" + paginaErro).forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("vinhos.jsp");
    }
}
