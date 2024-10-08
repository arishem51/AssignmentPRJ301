import { RouterModule, Routes } from '@angular/router';
import { Login } from './routes/login/Login.component';
import { Dashboard } from './routes/dashboard/Dashboard.component';
import { NgModule } from '@angular/core';
import { AuthGuard } from './services/guard.service';
import { PublicRoute } from './services/public.service';
import { Home } from './routes/home/Home.component';
import { ProductionPlan } from './routes/ProductionPlan/ProductionPlan.component';

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
        path: 'production-plan',
        component: ProductionPlan,
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
