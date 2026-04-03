import { NavigationBar } from 'capacitor-navigation-bar';

window.testEcho = () => {
    const inputValue = document.getElementById("echoInput").value;
    NavigationBar.echo({ value: inputValue })
}
