export class MenuConfig {

  constructor(){}

  public defaults: any = {
    header: {
      self: {},
      items: [
        {
          title:'Comparison',
          root: true,
          alignment: 'left',
          toggle: 'click',
          page: '',
          translate: "MENU.COMPARISON",
          submenu: [
            {
              title: 'Transaction Comparison',
              page: '/compare-transaction',
              translate: "MENU.COMPARISON"
            },

            {
              title: 'Module Information',
              page: '',
              translate: "MENU.INFORMATION"
            },
          ]
        },
      ],
    },
    aside: {
      self: {},
      items: [
        {
          title: "Comparison",
          page: "",
          toggle: 'click',
          translate: "MENU.INFORMATION",
          submenu: [
            {
              title: 'Transaction Comparison',
              page: '/compare-transaction',
              translate: "MENU.COMPARISON"
            },

            {
              title: 'Module Information',
              page: '',
              translate: "MENU.INFORMATION"
            },
          ]
        },
      ],
    },
  };

  public get configs(): any {
    // Add all the menu modules to the main array
    this.defaults.header.items = this.prepareComponentsUrls(
      this.defaults.header.items
    );

    return this.defaults;
  }

  // Recursively add the parent's page url to the child's page url
  private prepareComponentsUrls(jObject, identifier = "") {
    return jObject.map((obj) => {
      if (obj.hasOwnProperty("page")) {
        obj.page = identifier + obj.page;
        if (obj.hasOwnProperty("submenu")) {
          obj.submenu = this.prepareComponentsUrls(obj.submenu, obj.page);
        }
      }

      return obj;
    });
  }
}
