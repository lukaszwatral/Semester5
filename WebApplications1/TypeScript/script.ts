const msg: string = "Hello!";
alert(msg);


const styles: { [key: string]: string } = {
    "Styl 1": "./styles/style1.css",
    "Styl 2": "./styles/style2.css",
    "Styl 3": "./styles/style3.css",
}

dynamicSetStyle("Styl 1");
loadStyles();

function loadStyles(): void {
    const nav: HTMLElement = document.querySelector("nav") as HTMLElement;
    for (let style in styles) {
        const li: HTMLLIElement = document.createElement("li");
        const a: HTMLAnchorElement = document.createElement("a");
        a.onclick = () => switchStyle(style);
        a.textContent = style;
        a.style.cursor = "pointer";
        li.appendChild(a);
        nav.querySelector("ul")?.appendChild(li);
    }
}

function switchStyle(style: string): void {
    const link: HTMLLinkElement = document.querySelector(`link[rel="stylesheet"]`) as HTMLLinkElement;
    const newLink: HTMLLinkElement = document.createElement("link");
    newLink.rel = "stylesheet";
    newLink.href = styles[style];
    document.head.replaceChild(newLink, link);
}

function dynamicSetStyle(style: string): void {
    const link: HTMLLinkElement = document.createElement("link");
    link.rel = "stylesheet";
    link.href= styles[style];
    document.head.appendChild(link);
}
