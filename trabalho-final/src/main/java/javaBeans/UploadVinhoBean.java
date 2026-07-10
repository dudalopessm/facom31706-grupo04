package javaBeans;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadVinhoBean {

    private String diretorio;
    private long size;
    private String extensoesPermitidas;
    private String erro;
    private Map<String, String> parametros;
    private FileItem fileItem;

    private static final long MB = 1024 * 1024;

    public UploadVinhoBean() {
        this.size = 2;
        this.extensoesPermitidas = "jpg,jpeg,png";
        this.parametros = new HashMap<>();
    }

    public String getDiretorio() {
        return diretorio;
    }

    public void setDiretorio(String diretorio) {
        this.diretorio = diretorio;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getExtensoesPermitidas() {
        return extensoesPermitidas;
    }

    public void setExtensoesPermitidas(String extensoesPermitidas) {
        this.extensoesPermitidas = extensoesPermitidas;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public boolean temArquivo() {
        return fileItem != null;
    }

    public String getParametro(String nome) {
        return parametros.get(nome);
    }

    public boolean isPermission(String fileName) {
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        String[] exts = extensoesPermitidas.split(",");
        for (String e : exts) {
            if (e.trim().equals(ext)) {
                return true;
            }
        }
        return false;
    }

    public boolean processarUpload(HttpServletRequest request) {
        erro = null;
        parametros.clear();
        fileItem = null;

        if (!ServletFileUpload.isMultipartContent(request)) {
            erro = "O formulario deve ter enctype='multipart/form-data'";
            return false;
        }

        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload sfu = new ServletFileUpload(factory);
        sfu.setSizeMax(size * MB);

        try {
            List<FileItem> items = sfu.parseRequest(request);
            Iterator<FileItem> iter = items.iterator();

            while (iter.hasNext()) {
                FileItem item = iter.next();

                if (item.isFormField()) {
                    String nomeCampo = item.getFieldName();
                    String valorCampo = item.getString("UTF-8");
                    parametros.put(nomeCampo, valorCampo);
                } else {
                    String fileName = item.getName();

                    if (fileName == null || fileName.trim().isEmpty()) {
                        continue;
                    }

                    if (!isPermission(fileName)) {
                        erro = "Extensao nao permitida. Permitidas: " + extensoesPermitidas;
                        return false;
                    }

                    fileItem = item;
                }
            }

            return true;
        } catch (FileUploadException e) {
            if (e.getMessage() != null && e.getMessage().contains("size")) {
                erro = "Arquivo muito grande. Tamanho maximo: " + size + "MB";
            } else {
                erro = "Erro no upload: " + e.getMessage();
            }
            return false;
        } catch (Exception e) {
            erro = "Erro ao processar upload: " + e.getMessage();
            return false;
        }
    }

    public boolean salvarArquivo(ServletContext context, int idVinho) {
        if (fileItem == null) {
            return false;
        }

        try {
            String path = context.getRealPath("/" + diretorio);
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File arquivoFinal = new File(path + "/" + idVinho + ".jpg");
            fileItem.write(arquivoFinal);
            return true;
        } catch (Exception e) {
            erro = "Erro ao salvar arquivo: " + e.getMessage();
            return false;
        }
    }
}
