import { RouterModule, Routes } from '@angular/router';
import { Login } from './components/login/login.component';
import { Dashboard } from './components/dashboard/dashboard.component';
import { NgModule } from '@angular/core';
import { AuthGuard } from './services/guard.service';
import { PublicRoute } from './services/public.service';
import { Home } from './components/home/home.component';
import { Products } from './components/products/products.component';

export const routes: Routes = [
  {
    path: 'login',
    component: Login,
    canActivate: [PublicRoute],
  },
  {
    path: '',
    children: [
      {
        path: '',
        component: Home,
      },
      {
        path: 'products',
        component: Products,
      },
    ],
    component: Dashboard,
    canActivate: [AuthGuard],
  },
  {
    path: '**',
    redirectTo: '',
    pathMatch: 'full',
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
