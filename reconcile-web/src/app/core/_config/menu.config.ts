export class MenuConfig {

  constructor(){}

  public defaults: any = {
    header: {
      self: {},
      items: [
        {
          title: "Home",
          root: true,
          alignment: "left",
          page: "",
          translate: "MENU.HOME",
        },

        {
          title: "Information",
          root: true,
          alignment: "left",
          page: "",
          translate: "MENU.INFORMATION",
        },
      ],
    },
    aside: {
      self: {},
      items: [
        {
          title: "Home",
          root: true,
          alignment: "left",
          page: "",
          translate: "MENU.HOME",
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
