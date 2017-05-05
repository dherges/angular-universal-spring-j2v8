import { browser, element, by } from 'protractor';

export class HelloUniversalPage {
  navigateTo() {
    return browser.get('/');
  }

  getParagraphText() {
    return element(by.css('ngu-root h1')).getText();
  }
}
