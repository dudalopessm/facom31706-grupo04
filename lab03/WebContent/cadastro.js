var imagensCategoria = {
    tintos_encorpados: "imagens/tintos.jpg",
    brancos_leves: "imagens/brancos.jpg",
    champagnes: "imagens/champagnes.jpg",
    roses: "imagens/roses.jpg"
};

var nomesCategoria = {
    tintos_encorpados: "Tintos Encorpados e Clássicos",
    brancos_leves: "Brancos Leves e Aromáticos",
    champagnes: "Champagnes e Espumantes Finos",
    roses: "Rosés Delicados"
};

document.addEventListener("DOMContentLoaded", function () {
    var primeiroCampo = document.getElementById("nome_usuario");

    if (primeiroCampo) {
        primeiroCampo.addEventListener("focus", function () {
            this.style.backgroundColor = "#fffacd";
            this.placeholder = "Campo em foco — digite seu nome ou usuário";
        });

        primeiroCampo.addEventListener("blur", function () {
            this.style.backgroundColor = "";
            this.placeholder = "Digite seu nome ou usuário";
        });
    }
});

function validarCadastroCliente() {
    var nome = document.getElementById("nome_usuario").value.trim();
    var email = document.getElementById("email").value.trim();
    var senha = document.getElementById("senha").value.trim();
    var preferencia = document.getElementById("preferencia_vinho").value;

    if (nome === "" || email === "" || senha === "" || preferencia === "") {
        alert("Todos os campos do cadastro são obrigatórios. Preencha-os antes de enviar.");
        return false;
    }

    return true;
}

function cadastrarCliente() {
    if (!validarCadastroCliente()) {
        return;
    }

    var nome = document.getElementById("nome_usuario").value.trim();
    var email = document.getElementById("email").value.trim();
    var preferencia = document.getElementById("preferencia_vinho").value;
    var relatorio = document.getElementById("relatorio_cadastro");
    var imagemSelecao = document.getElementById("imagem_selecao");

    relatorio.innerHTML =
        "<h3>Relatório do Cadastro</h3>" +
        "<p><b>Nome/Usuário:</b> " + nome + "</p>" +
        "<p><b>E-mail:</b> " + email + "</p>" +
        "<p><b>Preferência de Vinho:</b> " + (nomesCategoria[preferencia] || "—") + "</p>";

    if (imagensCategoria[preferencia]) {
        imagemSelecao.src = imagensCategoria[preferencia];
        imagemSelecao.alt = "Imagem da preferência de vinho " + (nomesCategoria[preferencia] || "");
        imagemSelecao.style.display = "block";
        imagemSelecao.style.width = "250px";
        imagemSelecao.style.height = "250px";
        imagemSelecao.style.objectFit = "cover";
    } else {
        imagemSelecao.style.display = "none";
        imagemSelecao.src = "";
        imagemSelecao.alt = "";
    }
}
