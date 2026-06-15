const btn = document.getElementById("btn-popular");
const popStatus = document.getElementById("pop-status");

const INSERT_KEYS = {
    cat: "cf_insert_cat",
    vinho: "cf_insert_vinho",
    cliente: "cf_insert_cliente",
    pedido: "cf_insert_pedido"
};

document.querySelectorAll(".btn.inserir").forEach(b => {
    const key = INSERT_KEYS[b.getAttribute("onclick").match(/'(\w+)'\)/)[1]];

    if (localStorage.getItem(key) === "true") {
        disableInsert(b);
    }
});

function disableInsert(botao) {
    botao.disabled = true;
    botao.innerHTML = '<i class="ti ti-circle-check"></i> Inserido';
}

async function inserir(botao, url, key) {

    botao.disabled = true;
    botao.innerHTML = '<i class="ti ti-loader"></i> ...';

    try {

        const res = await fetch(url);

        if (res.ok) {
            localStorage.setItem(INSERT_KEYS[key], "true");
            disableInsert(botao);
        } else {
            throw new Error("Erro " + res.status);
        }

    } catch (e) {

        botao.disabled = false;
        botao.innerHTML = '<i class="ti ti-plus"></i> Inserir';
        alert("Erro ao inserir: " + e.message);

    }
}

function setPopulated() {
    btn.disabled = true;
    btn.innerHTML = '<i class="ti ti-circle-check"></i> Populado';
    popStatus.textContent = "Banco já foi populado.";
    popStatus.style.color = "#2a7a2a";
}

async function popularDb() {

    btn.disabled = true;
    btn.innerHTML = '<i class="ti ti-loader"></i> Aguarde...';
    popStatus.textContent = "";

    try {

        const res = await fetch("PopularServlet");
        const data = await res.json();

        if (data.ok) {

            setPopulated();
            popStatus.textContent = "Banco populado com sucesso!";

        } else {

            if (data.erro === "Banco ja foi populado.") {
                setPopulated();
            } else {
                throw new Error(data.erro || "Erro desconhecido");
            }

        }

    } catch (e) {

        btn.disabled = false;
        btn.innerHTML = '<i class="ti ti-database-import"></i> Popular database';
        popStatus.textContent = "Erro: " + e.message;
        popStatus.style.color = "#8b0000";

    }
}