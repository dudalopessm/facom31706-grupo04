const btn = document.getElementById("btn-popular");
const popStatus = document.getElementById("pop-status");

async function popularDb() {

    btn.innerHTML = '<i class="ti ti-loader"></i> Aguarde...';
    popStatus.textContent = "";

    try {

        const res = await fetch("PopularServlet");
        const data = await res.json();

        btn.innerHTML = '<i class="ti ti-database-import"></i> Popular database';

        if (data.ok) {
            popStatus.style.color = "#2a7a2a";
            popStatus.textContent = "Banco populado com sucesso!";
        } else if (data.erro === "Banco ja foi populado.") {
            popStatus.style.color = "#b5852a";
            popStatus.textContent = "O banco já foi populado.";
        } else {
            throw new Error(data.erro || "Erro desconhecido");
        }

    } catch (e) {

        btn.innerHTML = '<i class="ti ti-database-import"></i> Popular database';
        popStatus.style.color = "#8b0000";
        popStatus.textContent = "Erro: " + e.message;

    }
}