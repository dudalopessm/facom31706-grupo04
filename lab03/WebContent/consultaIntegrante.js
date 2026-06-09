var integrantesFotos = [
    { matricula: "12311BCC032", nome: "Anderson Gabriel", foto: "fotos/anderson.jpg" },
    { matricula: "12311BCC033", nome: "Eduarda Lopes", foto: "fotos/eu.jpg" },
    { matricula: "12311BCC019", nome: "Gabriel Augusto", foto: "fotos/gabriel4.jpg" },
    { matricula: "12311BCC013", nome: "Yan Lucas", foto: "fotos/yan.jpg" },
    { matricula: "12311BCC024", nome: "Lucas Matos", foto: "fotos/lucsd.jpg" },
    { matricula: "12411BCC102", nome: "Kamily Cristina", foto: "fotos/kamily.jpg" }
];

var indiceAtual = -1;

function buscarFoto() {
    var matricula = document.getElementById("matricula_busca").value;
    var resultado = document.getElementById("resultado_busca");
    var encontrado = false;

    switch (matricula) {
        case "12311BCC032":
            indiceAtual = 0;
            encontrado = true;
            break;

        case "12311BCC033":
            indiceAtual = 1;
            encontrado = true;
            break;

        case "12311BCC019":
            indiceAtual = 2;
            encontrado = true;
            break;

        case "12311BCC013":
            indiceAtual = 3;
            encontrado = true;
            break;

        case "12311BCC024":
            indiceAtual = 4;
            encontrado = true;
            break;

        case "12411BCC102":
            indiceAtual = 5;
            encontrado = true;
            break;

        default:
            if (matricula === "") {
                resultado.innerHTML =
                    "<p><b>Atenção:</b> digite uma matrícula antes de pesquisar.</p>";
            } else {
                resultado.innerHTML =
                    "<p><b>Matrícula não encontrada.</b> " +
                    "Nenhum integrante do grupo possui o número informado. " +
                    "Verifique a matrícula digitada e tente novamente.</p>";
            }
            break;
    }

    if (encontrado) {
        exibirFotoIntegrante(indiceAtual);
    }
}

function exibirFotoIntegrante(indice) {
    var resultado = document.getElementById("resultado_busca");
    var integrante = integrantesFotos[indice];

    resultado.innerHTML =
        "<h3>" + integrante.nome + "</h3>" +
        "<p>Matrícula: " + integrante.matricula + "</p>" +
        "<p><i>Clique na foto para ver o próximo integrante.</i></p>" +
        "<img id='foto_integrante' src='" + integrante.foto + "' alt='Foto de " + integrante.nome + "' width='200' style='cursor: pointer;'>";

    document.getElementById("foto_integrante").addEventListener("click", function () {
        indiceAtual = (indiceAtual + 1) % integrantesFotos.length;
        exibirFotoIntegrante(indiceAtual);
    });
}
