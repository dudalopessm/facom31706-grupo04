function buscarFoto() {
    var matricula = document.getElementById("matricula_busca").value;
    var resultado = document.getElementById("resultado_busca");

    switch (matricula) {
        case "12311BCC032":
            resultado.innerHTML =
                "<h3>Anderson Gabriel</h3>" +
                "<p>Matrícula: 12311BCC032</p>" +
                "<img src='fotos/anderson.jpg' alt='Foto de Anderson Gabriel Moura' width='200'>";
            break;

        case "12311BCC033":
            resultado.innerHTML =
                "<h3>Eduarda Lopes</h3>" +
                "<p>Matrícula: 12311BCC033</p>" +
                "<img src='fotos/eu.jpg' alt='Foto de Eduarda Lopes' width='200'>";
            break;

        case "12311BCC019":
            resultado.innerHTML =
                "<h3>Gabriel Augusto</h3>" +
                "<p>Matrícula: 12311BCC019</p>" +
                "<img src='fotos/gabriel4.jpg' alt='Foto de Gabriel Augusto' width='200'>";
            break;

        case "12311BCC013":
            resultado.innerHTML =
                "<h3>Yan Lucas</h3>" +
                "<p>Matrícula: 12311BCC013</p>" +
                "<img src='fotos/yan.jpg' alt='Foto de Yan Lucas Santos' width='200'>";
            break;

        case "12311BCC024":
            resultado.innerHTML =
                "<h3>Lucas Matos</h3>" +
                "<p>Matrícula: 12311BCC024</p>" +
                "<img src='fotos/lucsd.jpg' alt='Foto de Lucas Matos' width='200'>";
            break;

        case "12411BCC102":
            resultado.innerHTML =
                "<h3>Kamily Cristina</h3>" +
                "<p>Matrícula: 12411BCC102</p>" +
                "<img src='fotos/kamily.jpg' alt='Foto de Kamily Cristina' width='200'>";
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
}
