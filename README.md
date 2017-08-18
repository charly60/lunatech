# lunatech


API http permettant d'avoir access a différente données concernant les aeroports.
Vous pouvez personaliser vos variables d'environement afin de tester cette API.
Utiliser une requete http.POST sur l'URL "votreHost:votrePort/find"  avec pour contenu le nom ou le code d'un pays
afin d'en retirer tous les aeroports ratachés à ce pays.

Vous pouvez également récupérer les 10 pays ayant le plus d'aeroports ainsi que les 10 pays en ayant le moins en requêtant 
à l'aide d'un http.GET sur l'URL "votreHost:votrePort/report/number_of_airport".
La dernière fonctionnalité permet de récupérer les types de piste d'atterissage associées à chaque pays, informations obtenues
en requêtant à l'aide d'un http.GET sur "votreHost:votrePort/report/type_of_runway".
