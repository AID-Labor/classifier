import { Octokit } from "https://cdn.skypack.dev/octokit";

const select = document.getElementById("osSelect");
const platform = window.navigator.userAgent;
if (platform.toLowerCase().includes("mac")){
    select.value = "macOS";
} else if (platform.toLowerCase().includes("win")){
    select.value = "windows";
} else {
    select.value = "linux";
}

function setHidden(hide, element) {
    console.log("hide: ", hide, element);
    if (hide){
        element.setAttribute("hidden", "true");
    } else if (element.hasAttribute("hidden")) {
        element.removeAttribute("hidden");
    }
}

function selectOS() {
    const select = document.getElementById("osSelect");
    const selectedOS = select.value;
    const downloadMacOS = document.getElementById("downloads-macOS");
    const downloadLinux = document.getElementById("downloads-linux");
    const downloadWindows = document.getElementById("downloads-windows");
    setHidden(selectedOS !== "macOS", downloadMacOS);
    setHidden(selectedOS !== "linux", downloadLinux);
    setHidden(selectedOS !== "windows", downloadWindows);
}

async function loadAssets() {
    // Octokit.js
    // https://github.com/octokit/core.js#readme
    const octokit = new Octokit({ });
    const response = await octokit.request('GET /repos/{owner}/{repo}/releases/latest', {
        owner: 'AID-Labor',
        repo: 'classifier',
        headers: {
            //'X-GitHub-Api-Version': '2022-11-28'
        }
    });

    let listMac = document.getElementById("listMac");
    let listLinux = document.getElementById("listLinux");
    let listWindows = document.getElementById("listWindows");

    response.data.assets.forEach(asset => {
        const a = document.createElement("a");
        a.setAttribute('href', asset.browser_download_url);
        a.appendChild(document.createTextNode(asset.name));
        const li = document.createElement("li");
        li.appendChild(a);
        console.log(asset);
        if (asset.name.toLowerCase().includes("mac")) {
            listMac.appendChild(li);
        } else if (asset.name.toLowerCase().includes("linux")) {
            listLinux.appendChild(li);
        } else if (asset.name.toLowerCase().includes("win")) {
            listWindows.appendChild(li);
        }
    });
}

selectOS();
select.addEventListener("change", selectOS)
loadAssets();
selectOS();
