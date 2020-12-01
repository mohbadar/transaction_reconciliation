import { Injectable } from '@angular/core';
import { Router, ActivatedRouteSnapshot, CanActivate } from '@angular/router';
import { locale as enLang } from './core/_config/i18n/en';
import { locale as faLang } from './core/_config/i18n/fa';
import { locale as psLang } from './core/_config/i18n/ps';
import { TranslationService } from './core/_base/layout';

// This is to avoid node/types error, since it is used only once
// no need to install @types/node
declare var require: any;

// This guard is used to lead module specific translations when that module is loaded
// The translations should be seperated for calrity and maintanance
@Injectable({
    providedIn: 'root'
})
export class TranslateGuard implements CanActivate {

    constructor( protected router: Router, private translationService: TranslationService) { }

    canActivate(route: ActivatedRouteSnapshot): boolean {
        
        if(route.data.hasOwnProperty('moduleName')) {
            const moduleEn = this.loadModuleSpecificTranslations(route.data.moduleName, 'en');
            if(moduleEn) {
                enLang.data.M = moduleEn;
            }

            const moduleFa = this.loadModuleSpecificTranslations(route.data.moduleName, 'fa');
            if(moduleFa) {
                faLang.data.M = moduleFa;
            }

            const modulePs = this.loadModuleSpecificTranslations(route.data.moduleName, 'ps');
            if(modulePs) {
                psLang.data.M = modulePs;
            }

            this.translationService.loadTranslations(enLang, faLang, psLang);
        }

        return true;
    }

    loadModuleSpecificTranslations(moduleName, language) {
        // Get the module object from the file.
        try {
            const { module } = require('./core/_config/i18n/' + moduleName + '/' + language + '.ts');
            return module ? module : {};
        } catch (error) {
            return null
        }
    }
}
