import { HelloUniversalPage } from './app.po';

describe('hello-universal App', () => {
  let page: HelloUniversalPage;

  beforeEach(() => {
    page = new HelloUniversalPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('ngu works!');
  });
});
